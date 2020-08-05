package zapiObjects;

import jiraObjects.Issue;

public class ExecutionDetails {
	public String id;
	public int issueId;
	public int versionId;
	public int projectId;
	public String executionId;
	public int orderId;
	public String createdBy;
	public String createdByAccountId;
	public ExecutionStatus status;
	public String cycleName;
	public Issue[] defects;
	public String projectCycleVersionIndex;
	public String projectIssueCycleVersionIndex;
	
	public String getUpdateJson() {
		String json="{";
		
		json+="\"status\":{\"id\":"+status.id+"},";
		json+="\"projectId\":"+projectId+",";
		json+="\"issueId\":"+issueId+",";
		json+="\"versionId\":"+versionId;
		
		if(defects != null && defects.length>0) {
			json += ",\"defects\":[";
			for(int i =0; i < defects.length; i ++) {
				if(i>0) {
					json+=",";
				}
				json += "\""+defects[i].id+"\"";				
			}
			json += "]";
		}
		
		json+="}";
		return json;
	}
}