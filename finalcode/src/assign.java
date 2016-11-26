
import java.util.Scanner;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class assign {
	public String tweet;
	public double weight;
	public String label;
	public double ngramweight;
	public double pattern;
	public int word;
	public double punch;
	public double total;
	public double wordfeatureweight;
	public int pair[] = new int[10000];

	public String getsent() {
		return label;
	}
	
	public  void setwordfeatureweight(double wordweight)
	{
		    wordfeatureweight=wordweight;
	}
	
	public double getwordfeatureweight()
	{
		return wordfeatureweight;
	}

	public void setpair(int array[]) {
		pair = array;
	}

	public void displaypair() {
		for (int i = 0; i < pair.length; i++)
			System.out.print(pair[i] + " ");
	}

	public void makefeaturevector() {
		total = weight + ngramweight + pattern + punch;
	}

	public void setpunchuation(double pun) {
		punch = pun;
	}

	public double getpunchuation() {
		return punch;
	}

	public void setpattern(int pat) {
		pattern = pat;
	}

	public double getngramweight() {
		return ngramweight;
	}

	public double getpatternweight() {
		return pattern;
	}

	public void setlength(int length) {
		word = length;
	}

	public int getlength() {
		return word;
	}

	public void setpatternweight(double globalpattern) {
		// pattern=1+( pattern/(double)globalpattern);
		if(globalpattern==0)
			globalpattern=1;
		else
			pattern = 1 + (pattern / (double) globalpattern);

		// System.out.println(pattern);
	}
   
	public void settweet(String name) {
		tweet = name;

	}

	public String gettweet() {
		return tweet;
	}

	public void setlabel(String sentiment) {
		label = sentiment;

	}

	public void setweight(double word) {

		weight = word;

	}

	public double getweight() {

		return weight;

	}

	public void setngramweight(double word) {

		ngramweight = word;

	}

	public void display() {
		//System.out.println(tweet + "  " + label + " " + total + " "
				//+ ngramweight + " \n");

	}

	public String getlabel() {
		return label;
	}
	 static void shuffleArray(assign[] ar,int tweetcount)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = tweetcount-1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      assign a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }

}
