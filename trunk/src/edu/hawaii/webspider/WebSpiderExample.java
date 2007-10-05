package edu.hawaii.webspider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebLink;

/**
 * A demonstration class illustrating how to retrieve and process web pages using HttpUnit.
 * 
 * @author Philip Johnson modify by Jeffrey Lam
 */
public class WebSpiderExample {

  /** Contains the starting url for the crawl. */
  private final String startUrl;
  /** Create a space to store visited page. */
  private final static List<String> pageVisited = new ArrayList<String>();
  /** Create a space to store current page. */
  private final static List<String> pageCurrent = new ArrayList<String>();
  /** Initialize total number of links. */
  private static int totalOfLink = 0;
  /** Initialize most popular links to visited. */
  private static int visited = 0;
  /** Initialize temporary variable for most popular links to visited. */
  private static int tempVisited = 0;
  /** Initialize total number of links. */
  private static String visitedPages = "";
  /** Initialize total number of links. */
  private static String tempVisitedPages = "";
  /** Logging each process */
  private static Logger logging = Logger.getLogger("edu.hawaii.webspider");
  /** Initialize logger on and off. */
  private static boolean log;
  
  /**
   * A WebSpider crawls the web and returns info.
   * 
   * @param startUrl The home url for the crawl.
   */
  public WebSpiderExample(String startUrl) {
    this.startUrl = startUrl;
  }

  /**
   * Returns the number of links found at the startUrl.
   * 
   * @return The number of links.
   * @throws Exception if problems occur retrieving the startUrl.
   */
  public int getNumLinks() throws Exception {
	// create the conversation object which will maintain state for us
    WebConversation wc = new WebConversation();
    WebRequest request = new GetMethodWebRequest(this.startUrl);
    WebResponse response = wc.getResponse(request);
    return response.getLinks().length;
  }

  /**
   * Find number of link within a range of page.
   * This code inspired by Jon Lao code
   * 
   * @param link the URL
   * @param numPages number of page to search
   * @throws Exception
   */
  public static void totalLinks(String link, int numPages) throws Exception {
	  com.meterware.httpunit.HttpUnitOptions.setExceptionsThrownOnScriptError(false);
	  com.meterware.httpunit.HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
	  com.meterware.httpunit.HttpUnitOptions.setScriptingEnabled(false);
	  
	  try {
		for(int i = 0; i <= numPages; i++) {
		  WebConversation wc = new WebConversation();
		  WebRequest request = new GetMethodWebRequest(link);
		  WebResponse response = wc.getResponse(request);
	      WebLink[] newWebLink = response.getLinks();
		  pageVisited.add(link);
		  for(int j = 0; j < newWebLink.length; j++) {
			if(!(pageVisited.contains(newWebLink[j]))) {
		      pageCurrent.add(newWebLink[j].getRequest().getURL().toString());
		      totalOfLink++;
     	  }
	    }
		  link = pageCurrent.remove(0);
		}
		if(log)
	       logging.log(Level.INFO, "Total number of links : "+totalOfLink);
		else
		   System.out.println("Total number of Links :"+totalOfLink+" Links");
	}
	catch(Exception e){
		if(log)
		  logging.log(Level.INFO, "Exception: : " + e);
		else
	      System.out.println("Exception: " + e);
	  }
  }
  
  /**
   * Find most popular link.
   * This is inspired by Chiao-Fen Zielinski code.
   * 
   * @param link the URL
   * @param numPages number of page to search
   * @throws Exception
   */
  public static void mostPopularLinks(String link, int numPages) throws Exception {	    
	  com.meterware.httpunit.HttpUnitOptions.setExceptionsThrownOnScriptError(false);
	  com.meterware.httpunit.HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
	  com.meterware.httpunit.HttpUnitOptions.setScriptingEnabled(false);
	  
	  // Get visited link and store it into array.
	  try {
	    for(int i = 0; i <= numPages; i++) {
		  WebConversation wc = new WebConversation();
		  WebRequest request = new GetMethodWebRequest(link);
		  WebResponse response = wc.getResponse(request);
	      WebLink[] newWebLink = response.getLinks();
		  pageVisited.add(link);
		  for(int j = 0; j < newWebLink.length; j++) {
		    if(!(pageVisited.contains(newWebLink[j]))) {
		       pageCurrent.add(newWebLink[j].getRequest().getURL().toString());
			}
		  }
		  link = pageCurrent.remove(0);
        }
	  }
	  // error checking
	  catch(Exception e){
		if(log)
		   logging.log(Level.INFO, "Exception: : " + e);
		else
		   System.out.println("Exception: " + e);
      }
	  // find a popular link in array
      try{
	    for (int i = 0; i < numPages; i++) {
	      for (int j = 0; j < numPages; j++) {
	        if (pageVisited.get(i).equals(pageVisited.get(j))) {
	          tempVisited++;
	          tempVisitedPages = pageVisited.get(i);
	        }
	      }
	      if (tempVisited > visited) {
	    	  visited = tempVisited;
	          visitedPages = tempVisitedPages;
	      }
	      tempVisited = 0;
	      tempVisitedPages = "";

	    }
	    if(log)
	       logging.log(Level.INFO, "The most popular page is : " + visitedPages + " = " + visited + " visited");
	    else
	       System.out.println("The most popular page is " + visitedPages + " = " + visited + " visited");
	  }
	  catch(Exception e){
	 	if(log)
		   logging.log(Level.INFO, "Exception: : " + e);
		else
		System.out.println("Exception: " + e);
	  }
  }

  /**
   * Retrieves the httpunit home page and counts the number of links on it.
   * 
   * @param args Ignored.
   * @throws Exception If problems occur.
   */
  public static void main(String[] args) throws Exception {
	if(args.length == 0) {
       totalLinks("http://www.hackstat.org", 100);
	}
	if(args.length == 1 && args[0].equals("-totallinks")) {
	   totalLinks("http://www.hackstat.org", 100);
	}
	if(args.length == 1 && args[0].equals("-mostpopular")) {
	   mostPopularLinks("http://www.hackstat.org", 100);
	}
    if(args.length == 2 && args[0].equals("-totallinks")) {
       totalLinks(args[1], 100);
    }
    if(args.length == 2 && args[0].equals("-mostpopular")) {
       mostPopularLinks(args[1], 100);
    }
    if(args.length == 3 && args[0].equals("-totallinks")) {
       totalLinks(args[1],Integer.parseInt(args[2]));
    }
    if(args.length == 3 && args[0].equals("-mostpopular") ) {
       mostPopularLinks(args[1],Integer.parseInt(args[2]));
    }
    if(args.length == 4 && args[0].equals("-totallinks") && args[3].equals("-logging")) {
       log = true;
       totalLinks(args[1],Integer.parseInt(args[2]));
    }
    if(args.length == 4 && args[0].equals("-mostpopular") && args[3].equals("-logging")) {
       log = true;
       mostPopularLinks(args[1],Integer.parseInt(args[2]));
    }
  }
}
