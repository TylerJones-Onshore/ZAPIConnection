package apiTests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import jira.Client;
import jiraObjects.BrowserEnum.Browsers;
import jiraObjects.CreateIssueDetails;
import jiraObjects.DefectTypeEnum.DefectType;
import jiraObjects.DeviceEnum.Devices;
import jiraObjects.EnvironmentsEnum.Environments;
import jiraObjects.Issue;
import jiraObjects.Project;
import jiraObjects.SeverityEnum.Severity;
import jiraObjects.Version;
import jiraObjects.IssueTypeEnum;
import jiraObjects.PriorityEnum.Priority;
import zapi.ZapiClient;
import zapiObjects.CycleDetails;
import zapiObjects.Execution;
import zapiObjects.ExecutionStatus;
import zapiObjects.ExecutionStatusEnum;
import zapiObjects.ProjectCycles;

public class SimulatedExecution {
	Client jira = new Client("tjones@aarp.org","0yw8ZD5F9Xn7p2gVAF8pC588");
	ZapiClient zapi = new ZapiClient();

	@Test
	public void executionExample() {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";
		String accId = "5bfc155710c30e4ac8c8a502";
		
		jira.baseUri = "https://aarpqmo.atlassian.net";
		jira.contentType = "application/json";
		zapi.setAccessKey(accessKey);
		zapi.setSecretKey(secretKey);
		zapi.setAccountId(accId);
		zapi.contentType = "application/json";
		Map<String, String> tests = new HashMap<String,String>();
		Map<String, String> testsResults = new HashMap<String,String>();
		
		tests.put("TES-7", "Version 1");
		tests.put("TES-8", "Version 1");
		tests.put("TES-9", "Version 1");
		tests.put("TES-23", "Version 1");
		tests.put("TES-24", "Version 1");
		tests.put("TES-25", "Version 1");
		tests.put("TES-26", "Version 1");
		testsResults.put("TES-7", "Pass");
		testsResults.put("TES-8", "Pass");
		testsResults.put("TES-9", "Fail");
		testsResults.put("TES-23", "Pass");
		testsResults.put("TES-24", "Fail");
		testsResults.put("TES-25", "Fail");
		testsResults.put("TES-26", "Pass");
		
		String errorMessage = "";

		try {
			throw new Exception("Error message example.");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		
		
		Map<String,ProjectCycles> createdCycles = new HashMap<String,ProjectCycles>();
		for(String testResultKey:testsResults.keySet()) {
			Project p;
			String targetVersionId="";
			Issue createdTicket=null;
			String status = testsResults.get(testResultKey);
			ProjectCycles targetCycle = null;
			p = jira.getProject(testResultKey.split("-")[0]);
			
			if(status.equals("Fail")) {
				createdTicket = reportBug(testResultKey+" execution failure.",errorMessage,p);
			}

			for(Version v:p.versions) {
				if(v.name.equals(tests.get(testResultKey))) {
					targetVersionId = v.id;
					break;
				}
			}
		    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		    String date = formatter.format(new Date());
			
		    if(!createdCycles.containsKey(targetVersionId)) {
		    	createdCycles.put(targetVersionId, zapi.createCycle(date, targetVersionId, p.id));
		    }
		    targetCycle = createdCycles.get(targetVersionId);
			try {
				zapi.addTestToCycle(targetCycle.id, p.id, targetVersionId, new String[] {testResultKey});
			} catch (Exception e) {
				e.printStackTrace();
			}
			CycleDetails cycleDetails = zapi.getCycle(p.id, targetVersionId, targetCycle.id);
			Execution execution = null;
			for(Execution e:cycleDetails.searchObjectList) {
				if(e.issueKey.equals(testResultKey)) {
					execution = e;
					break;
				}
			}
			if(execution != null) {
				execution.execution.status = new ExecutionStatus();
				execution.execution.status.id = ExecutionStatusEnum.getStatusCode(testsResults.get(testResultKey));
				if(createdTicket!=null && execution.execution.defects.length<1) {
					execution.execution.defects = new Issue[1];
					execution.execution.defects[0] = createdTicket;
				}
				else if(createdTicket != null && execution.execution.defects.length>0) {
					Issue[] temp = new Issue[execution.execution.defects.length+1];
					temp[execution.execution.defects.length-1] = createdTicket;
					execution.execution.defects = temp;
				}
				zapi.updateExecution(execution.execution.id, execution.execution);
			}
		}
	}
	
	public Issue reportBug(String summary, String errorMessage, Project p) {
		Issue createdIssue = null;
		try {

			CreateIssueDetails ci = new CreateIssueDetails();
			ci.summary = summary;
			ci.description = errorMessage;
			ci.iCode = IssueTypeEnum.TypeCode.BUG;
			ci.project = p.id;
			ci.customFields = new HashMap<String, String>();
			Browsers browser = Browsers.CHROME;
			ci.customFields.put(browser.getFieldId(), "[{\"value\":\"" + browser.getBrowser() + "\"}]");
			Devices device = Devices.DESKTOP;
			ci.customFields.put(device.getFieldId(), "[{\"value\":\"" + device.getDevice() + "\"}]");
			Environments environment = Environments.QA;
			ci.customFields.put(environment.getFieldId(),
					"[{\"value\":\"" + environment.getEnvironment() + "\"}]");
			Priority priority = Priority.LOW;
			ci.customFields.put(priority.getFieldId(), "{\"value\":\"" + priority.getPriority() + "\"}");
			Severity s = Severity.LOW;
			ci.customFields.put(s.getFieldId(), "{\"value\":\"" + s.getSeverity() + "\"}");
			DefectType dType = DefectType.BUG;
			ci.customFields.put(dType.getFieldId(), "{\"value\":\"" + dType.getDefectType() + "\"}");
			// ToDo::Create a ticket and track the jira key;
			createdIssue = jira.createIssue(ci);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return createdIssue;
	}
}