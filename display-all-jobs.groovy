import groovy.lang.*;
import groovy.util.*;


jobCounter(Hudson.instance.items)

def jobCounter(items){
  
  if(items!= null){
    for(item in items){
      println("====================================");
      println("Job Name:\t"+item.displayName);
      println("Last Success:\t"+item.getLastSuccessfulBuild());
      println("Last Fail:\t"+item.getLastFailedBuild());
    }
  }
}
