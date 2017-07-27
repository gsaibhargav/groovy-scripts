import groovy.lang.*;
import groovy.transform.Field;
import groovy.util.*;
import java.io.*;
import hudson.triggers.*;

//jobCounter(Hudson.instance.items); Does not read Folders
jobCounter(Jenkins.instance.getAllItems(AbstractProject.class));

@Field int jobs=gits=gerrits=svns=gerrit_triggers=polltriggers=0;
@Field def config;

def getConfig(item){
  def configXml =  item.getConfigFile();
  def file=configXml.getFile();
  def StringBuilder sb= new StringBuilder();

  InputStream is =  new FileInputStream(file);
  BufferedReader br=new BufferedReader(new InputStreamReader(is));

  String reader;
  while((reader=br.readLine())!=null){
    sb.append(reader);
    }
   
  //Xml print;println(sb+"\n\n\n");

  config = new XmlParser().parseText(sb.toString());

  return;
}

def getSCM(){

  String sourceControl = config.scm.@plugin;

  if(sourceControl.contains("git")){
    String git_gerrit = config.scm.userRemoteConfigs."hudson.plugins.git.UserRemoteConfig".text();
    if(git_gerrit.contains("gerrit")){
      gerrits=gerrits+1;
      //println(git_gerrit);
      return "Gerrit";
    }
    else if(git_gerrit.contains("source-")){
      gits=gits+1;
      //println(git_gerrit);
      return "GIT";
    }
    else{
     // println(git_gerrit);
    }
  }
  else if(sourceControl.contains("subversion")){
    svns=svns+1;
    //println(config.scm);
    return "SVN";
  }
  else{
    println("Different Repo/Configured Null");
  }
  return;
}

def getJobTrigger(item){
  String triggers=item.getTriggers();
  if(triggers.contains("GerritTrigger")){
   gerrit_triggers=gerrit_triggers+1
   return "Gerrit"
  }
  if(triggers.contains("SCM")){
    polltriggers=polltriggers+1
    return "Polling/Timer"
  }
  return;
}

def getGerritTriggerConfig(){
  String compareType=config.triggers.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger'.gerritProjects.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject'.compareType.text();
  if(compareType!=null){
    String pattern=config.triggers.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger'.gerritProjects.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject'.pattern.text();
    println("Trigger:");
    println("\t Project Type:\t"+compareType+"\t Project Pattern:\t"+pattern)
    String branchType=config.triggers.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger'.gerritProjects.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject'.branches.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.Branch'.compareType.text();
    if(branchType!=null){
      String branchPattern=config.triggers.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger'.gerritProjects.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject'.branches.'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.Branch'.pattern.text();
      println("\t Branch type:\t"+branchType+"\t Branch Pattern:\t"+branchPattern);
    }
  }
}

def jobCounter(items){
  if(items!= null){
    for(item in items){
      jobs=jobs+1;
      println("==================================================");
      println("Job Name:\t"+item.displayName);
      getConfig(item)
      String scm=getSCM()
      println("Source Control:\t"+scm);
      String jobTrigger=getJobTrigger(item)
      if(jobTrigger=="Gerrit"){
        getGerritTriggerConfig()
      }
      else{
        TriggerDescriptor SCM_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(SCMTrigger.class)
        assert SCM_TRIGGER_DESCRIPTOR != null;

        def trigger = item.getTriggers().get(SCM_TRIGGER_DESCRIPTOR)
        if(trigger != null){
          String[] parts = trigger.spec.split(" ");
          println(parts);
        }
      }
    }
    println("===========Summary=============");
    println("Total Number of Jobs:\t"+jobs);
    println("Gerrit Trigger Jobs:\t"+gerrit_triggers);
    println("Polling SCM/Scheduled Jobs:\t"+polltriggers);
  }
}

