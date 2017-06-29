import groovy.lang.*;
import groovy.transform.Field;
import groovy.util.*;
import java.io.*;

//jobCounter(Hudson.instance.items); Does not read Folders
jobCounter(Jenkins.instance.getAllItems(AbstractProject.class));
@Field int jobs=gits=gerrits=svns=0;

def getJobSCM(item){

  def configXml =  item.getConfigFile();
  def file=configXml.getFile();
  def StringBuilder sb= new StringBuilder();
        
  InputStream is =  new FileInputStream(file);
  BufferedReader br=new BufferedReader(new InputStreamReader(is));
      
  String reader;
  while((reader=br.readLine())!=null){
    sb.append(reader);
    }
  //println(sb+"\n\n\n");
            
  def config = new XmlParser().parseText(sb.toString());
  //println(config.scm);
  
  String sourceControl = config.scm.@plugin;
  //println(sourceControl);
  
  
  if(sourceControl.contains("git")){
    String git_gerrit = config.scm.userRemoteConfigs."hudson.plugins.git.UserRemoteConfig".text();
    
    if(git_gerrit.contains("gerrit")){
      gerrits=gerrits+1;
      println(git_gerrit);
      return "Gerrit";
    }
    else if(git_gerrit.contains("source-")){
      gits=gits+1;
      println(git_gerrit);
      return "GIT";
    }
    else{
      println(git_gerrit);
    }
  }
  else if(sourceControl.contains("subversion")){
    svns=svns+1;
    println(config.scm);
    return "SVN";
  }
  else{
    println("Different Repo/Configured Null");
  }
  return;
}

def jobCounter(items){
  
  if(items!= null){
    for(item in items){
      jobs=jobs+1;
      println("==================================================");
      String sourceControl=getJobSCM(item); 
      println("Job Name:\t"+item.displayName);
      println("Source Control:\t"+sourceControl);
      println("Last Success:\t"+item.getLastSuccessfulBuild());
      println("Last Fail:\t"+item.getLastFailedBuild());
    }
  }
  println("*************************************************************");
  println("** Total Number Of Jobs **\t"+jobs);
  println("** Jobs Using GIT **\t\t"+gits);
  println("** Jobs Using Gerrit **\t\t"+gerrits);
  println("** Jobs Using SVN **\t\t"+svns);
}
      
