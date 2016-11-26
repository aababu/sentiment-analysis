import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import libsvm.LibSVM;
import libsvm.svm_parameter;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.FileHandler;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

import libsvm.LibSVM;
import libsvm.svm_model;
import libsvm.svm_parameter;
public class main {
	static String[] count = new String[50000];
	// static ArrayList<String>count=new ArrayList<>();
	// static ArrayList<Integer>occurance=new ArrayList<>();
	// static ArrayList<Integer>freq=new ArrayList<>();
	static int[] freq = new int[50000];
	static int[] occurance = new int[50000];
	static String[] ngrams = new String[50000];
	// static ArrayList<String>ngrams=new ArrayList<>();
	static int wordcnt = 0, tweetcount, countngram = 0, wordlistcnt = 0;
	static int[] noccurance = new int[50000];
	// static ArrayList<Integer>noccurance=new ArrayList<>();
	static int globalpattern = 0;
	static double maxw = -99999;
	static double maxword = -9999;
	static double maxnw = -99999;
	static double maxpw = -99999;
	static double maxpunchw = -9999;
	static double minw = 99999;
	static double minnw = 99999;
	static double minpw = 99999;
	static double minpunchw = 9999;
	static double minword = 9999;

	static int[][] pair = new int[50000][10000];
	// static List<List<Integer>>listoflist=new ArrayList<List<Integer>>();
	// static List<List<Integer>>listoflistfinal=new ArrayList<List<Integer>>();
	static int[][] finalpair = new int[10000][10000];
	static double euclid[][] = new double[10000][10000];
	static double final_accuracy = 0;
	static String[] parameter = new String[10000];
	static int[] actual_level = new int[10000];
	static String[] parameter1 = new String[10000];
	static String[] parameter2 = new String[10000];
	static double[][] pcaparameter = new double[10000][5];
	static String[] wordlist = new String[50000];
	static double[] wordstrength = new double[20000];
	static double finalprecession = 0;
	static double finalrecall = 0;
	static double finalfscore = 0;
	static String []stopword=new String[500];
	static int stopwordcnt=0;
	static double []c={0.3125,0.0625,0.125,0.25,0.5,1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768};
	static double[]gamma={.00003051757,0.00006103515,.00012207031,0.00024414062,0.00048828125,0.0009765625,0.001953125,.00390625,.0078125,.015625,.03125,.0625,.125,.25,.5,1,2,4,8};

	// caculate word feature start

	public static void wordcount(String tweet) {

		Scanner scanner = new Scanner(tweet);
		while (scanner.hasNext()) {
			
			
			int hash = 0;
			String nextToken = scanner.next();
			for(int j=0;j<stopwordcnt;j++)
			{
				if(nextToken.equalsIgnoreCase(stopword[j]))
					{
					hash++;
					break;
					}
			}

			int found = 0;
			if(hash==0){
			for (int i = 0; i < wordcnt; i++) {
				if (nextToken.equalsIgnoreCase(count[i])) {

					occurance[i] = occurance[i] + 1;
					// occurance.add(i,occurance.get(i)+1);
					// System.out.println(occurance[i]+".."+count[i]);
					found++;
					break;
				}

			}
			if (found == 0) {
				// count.add(wordcnt,nextToken);
				// count([wordcnt],nextToken);
				
				
				count[wordcnt] = nextToken;
				occurance[wordcnt] = occurance[wordcnt] + 1;
				// occurance.add(wordcnt,1);
				wordcnt++;

			}
			}

		}

	}

	public static double setweight(String word) {
		double total = 0;
		
		Scanner scanner = new Scanner(word);
		while (scanner.hasNext()) {
			int hash=0;
			
			
			String nextToken = scanner.next();
			for(int j=0;j<stopwordcnt;j++)
			{
				if(nextToken.equalsIgnoreCase(stopword[j]))
					{
					hash++;
					break;
					}
			}
			
			if(hash==0)
			total = total + 1.0 / (double) (getoccurance(nextToken));

		}
		return total;

	}

	public static int getoccurance(String word) {
		int i;
		for (i = 0; i < wordcnt; i++) {
			if (count[i].equalsIgnoreCase(word)) {
				break;
			}
		}
		// System.out.println(occurance[i]+"\n");
		return occurance[i];
	}

	// caculate word feature end

	// calculate ngram feature start

	public static void countngramoccurance(String tweet) {

		// System.out.println("countngram-------------------");

		int found = 0;
		for (int i = 0; i < countngram; i++) {
			if (tweet.equalsIgnoreCase(ngrams[i])) {
				noccurance[i] = noccurance[i] + 1;
				// noccurance.add(i,noccurance.get(i)+1);
				// System.out.println(noccurance[i]+".."+ngrams[i]);
				found++;
				break;
			}

		}
		if (found == 0) {
			ngrams[countngram] = tweet;
			// ngrams.add(countngram,tweet);
			// noccurance.add(countngram,1);
			noccurance[countngram] = noccurance[countngram] + 1;
			// System.out.println(ngrams[countngram]+"   "+noccurance[countngram]);
			countngram++;

		}

	}

	public static int getngramoccurance(String word) {
		int i;
		// System.out.println(word+"\n");
		for (i = 0; i < countngram; i++) {
			if (ngrams[i].equalsIgnoreCase(word)) {
				break;
			}
		}
		// System.out.println(occurance[i]+"\n");
		return noccurance[i];
	}

	public static double setngramweight(String word) {

		double total = 0;

		Scanner scanner = new Scanner(word);
		String message4 = "";
		int cnt_2_5 = 0;
		while (scanner.hasNext()) {
			String nextToken = scanner.next();

			// message4=message4+" "+nextToken;
			if (cnt_2_5 == 5) {

				// total=total+1.0/(double)getngramoccurance(message4);
				// System.out.println(getngramoccurance(message4)+"\n");
				// total=total+1.0/(double)countngram;
				double res = getngramoccurance(message4);
				// System.out.println(message4+"  "+res);
				if (res == 0)
					total = 0;
				else
					total = total + 1.0 / (double) res;

				message4 = "";
				cnt_2_5 = 0;
			} else {
				cnt_2_5++;
				message4 = message4 + " " + nextToken;

			}

		}

		return total;

	}

	public static void setfrequency(int value, int index) {
		double upperfh = .001 * wordcnt;
		double lowerfc = .1 * wordcnt;
		// System.out.println("boundary----------"+upperfh+"  "+lowerfc);
		if ((value > upperfh) && value < ((upperfh + lowerfc) / 2))
			// freq.add(index,9999);
			freq[index] = 9999;

		else if (value > ((upperfh + lowerfc) / 2) && value < lowerfc)
			freq[index] = -9999;
		else
			freq[index] = 0;

	}

	public static int getfrequency(String word) {
		int i;
		for (i = 0; i < wordcnt; i++) {
			if (word.equalsIgnoreCase(count[i]))
				break;
		}
		return freq[i];
	}

	public static int countpatterfeature(String word)

	{
		int cnt1 = 0;
		int cnt2=0;
		int cnt3=0;
		int ans;
		int notfound = 0;
		Scanner scanner = new Scanner(word);
		while (scanner.hasNext()) {
			String nextToken = scanner.next();
			 ans=getfrequency(nextToken);
			/*if (cnt == 0 && ((getfrequency(nextToken)) != 9999)) {
				notfound++;
				break;
			} else if (cnt == 6 && getfrequency(nextToken) != -9999) {
				notfound++;
				break;
			} else {
				if (getfrequency(nextToken) != 0) {
					notfound++;
					break;
				} else
					cnt++;
			}
			*/
			if(ans==9999)
				cnt1++;
			else if(ans==-9999)
				cnt2++;
			else 
				cnt3++;

		}
		System.out.println(cnt1+" "+cnt2);
		if (cnt1>=5 && cnt2>=2)
			return 1;
		else
			return 0;

	}

	// calculate patternfeature end

	// calculate punchuationfeature start

	public static double punchuationfeature(int number) {

		// System.out.println(number+"  "+maxw+"  "+maxnw+"   "+maxpw);
		return ((number * 3) / (double) (maxw + maxnw + maxpw));

	}

	public static double wordfeature(String tweet) {

		double total = 0;
		Scanner scanner = new Scanner(tweet);
		while (scanner.hasNext()) {

			String nextToken = scanner.next();

			int found = 0;
			for (int i = 0; i < wordlistcnt; i++) {
				if (nextToken.equalsIgnoreCase(wordlist[i])) {
					total = total + wordstrength[i];
					found++;
					break;
				}

			}

		}

		return total;

	}

	public static boolean isnumber(String str) {
		int i = 0;
		for (; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {

		int check = 0;
		File file1 = new File("wordstrength.txt");
		FileReader fileReader1 = new FileReader(file1);
		BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
		StringBuffer stringBuffer1 = new StringBuffer();
		String line1;
		while ((line1 = bufferedReader1.readLine()) != null) {
			// stringBuffer.append(line);
			// stringBuffer.append("\n");
			Scanner scanner1 = new Scanner(line1);
			// String message1="";
			String sentimentlabel1 = "";
			while (scanner1.hasNext()) {
				String nextToken = scanner1.next();
				if (check==0) {
					wordlist[wordlistcnt] = nextToken;
				//	wordstrength[wordlistcnt]=-1;
					//wordlistcnt++;
					check = 1;
				} else {
					//wordlist[wordlistcnt] = nextToken;
					//wordstrength[wordlistcnt]=-1;
					// System.out.println("weight:"+nextToken);
					wordstrength[wordlistcnt] = Double.parseDouble(nextToken);
				//	if(Double.parseDouble(nextToken)>0)
					//	wordstrength[wordlistcnt]=1;
					//else
						//wordstrength[wordlistcnt]=-1;
					wordlistcnt++;
					check = 0;
				}

			}
		}
		
		System.out.println(wordlistcnt);
		
		File file2 = new File("stopword.txt");
		FileReader fileReader2 = new FileReader(file1);
		BufferedReader bufferedReader2 = new BufferedReader(fileReader1);
		StringBuffer stringBuffer2 = new StringBuffer();
		String line2;
		while ((line2 = bufferedReader1.readLine()) != null) {
			// stringBuffer.append(line);
			// stringBuffer.append("\n");
			Scanner scanner1 = new Scanner(line2);
			{
				stopword[stopwordcnt]=scanner1.next();
				stopwordcnt++;
				
			}
			
				}

			
		
		
		
		

		assign[] x = new assign[10000];
		for (int i = 0; i < 10000; i++)
			x[i] = new assign();
		String message, message1 = "", message3 = "", message5 = "";
		int cnt = 0;

		int count_2_5 = 0;
		int count_1_6 = 0;
		int localpattern = 0;
		int tweetlength = 0;

		double ans;

		File file = new File("input500.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			// stringBuffer.append(line);
			// stringBuffer.append("\n");
			Scanner scanner = new Scanner(line);
			// String message1="";
			String sentimentlabel = "";
			while (scanner.hasNext()) {
				String nextToken = scanner.next();

				if (nextToken.length() > 8) {

					if ((nextToken.charAt(2) == ':' && nextToken.charAt(5) == ':')
							|| nextToken.charAt(1) == '2'
							|| nextToken.charAt(1) == '0'
							|| nextToken.charAt(1) == '4'
							|| nextToken.charAt(4) == '-'
							|| isnumber(nextToken)) {
						if (nextToken.charAt(0) == '"'
								&& nextToken.charAt(1) == '0'
								&& nextToken.charAt(2) == '"') {
							sentimentlabel = "negative";

						}
						// else
						// if(nextToken.charAt(0)=='"'&&nextToken.charAt(1)=='2'&&nextToken.charAt(2)=='"')
						// sentimentlabel="neutral";
						else if (nextToken.charAt(0) == '"'
								&& nextToken.charAt(1) == '4'
								&& nextToken.charAt(2) == '"') {
							sentimentlabel = "positive";

						}

						if (nextToken.charAt(2) == ':'
								&& nextToken.charAt(5) == ':') {
							message = (message1);

							if (sentimentlabel.equalsIgnoreCase("positive")
									|| sentimentlabel
											.equalsIgnoreCase("negative")) {
								wordcount(message);
								// if(count_2_5 !=0){
								// ngrams[countngram++]=message3;
								// countngramoccurance(message3);
								// count_2_5=0;}
								// ans=setweight(message);
								// x[cnt].setweight(ans);
								// System.out.println(message);
								x[tweetcount].settweet(message);
								x[tweetcount].setlabel(sentimentlabel);
								// x[tweetcount].setpattern(localpattern);
								x[tweetcount].setlength(tweetlength);

								x[tweetcount]
										.setwordfeatureweight(wordfeature(message));

								tweetcount++;
							}

							message1 = "";
							message3 = "";
							count_2_5 = 0;
							count_1_6 = 0;
							localpattern = 0;
							tweetlength = 0;
						}

					} else {
						message1 = message1 + " " + nextToken;
						message3 = message3 + " " + nextToken;
						count_2_5++;
					}

				} else {
					message1 = message1 + " " + nextToken;
					tweetlength++;
					if (count_2_5 == 5) {
						// ngrams[countngram++]=message3;
						// System.out.println(message3);
						countngramoccurance(message3);

						message3 = "";
						count_2_5 = 0;
					} else {
						count_2_5++;
						message3 = message3 + " " + nextToken;

					}

				}

			}

		}

		// System.out.println("loop end");
		//x[0].shuffleArray(x,tweetcount);
		


		for (int i = 0; i < wordcnt; i++) {

			setfrequency(occurance[i], i);
			// System.out.println(count[i]+"..."+occurance[i]+".."+freq[i]+"\n");
			// x[i].display();
		}

		for (int i = 0; i < tweetcount; i++) {
			String message2 = x[i].gettweet();
			// System.out.println(message2+"\n");
			Scanner scanner = new Scanner(message2);
			count_1_6 = 0;
			while (scanner.hasNext()) {
				String nextToken = scanner.next();
				if (count_1_6 == 6) {
					int result = countpatterfeature(message5);
					if (result == 1) {
						localpattern++;
						globalpattern++;
					}
					message5 = "";
					count_1_6 = 0;

				} else {
					count_1_6++;
					message5 = message5 + " " + nextToken;
				}

			}
			// System.out.println(localpattern);
			x[i].setpattern(localpattern);
			double ans1 = setweight(message2);
			x[i].setweight(ans1);
			ans1 = setngramweight(message2);
			// System.out.println(ans1+"\n");
			x[i].setngramweight(ans1);

		}
		// System.out.println("loop end1");
		for (int i = 0; i < tweetcount; i++) {
			x[i].setpatternweight(globalpattern);
		}

		for (int i = 0; i < tweetcount; i++) {

			if (x[i].getweight() > maxw)
				maxw = x[i].getweight();
			if (x[i].getwordfeatureweight() > maxword)
				maxword = x[i].getwordfeatureweight();

			if (x[i].getngramweight() > maxnw)
				maxnw = x[i].getngramweight();
			if (x[i].getpatternweight() > maxpw)
				maxpw = x[i].getpatternweight();
			if (x[i].getweight() < minw)
				minw = x[i].getweight();
			if (x[i].getngramweight() < minnw)
				minnw = x[i].getngramweight();
			if (x[i].getpatternweight() < minpw)
				minpw = x[i].getpatternweight();
			if (x[i].getwordfeatureweight() < minword)
				minword = x[i].getwordfeatureweight();

			// System.out.println(maxw+" "+maxnw+" "+maxpw+" "+maxword);

		}

		for (int i = 0; i < tweetcount; i++) {
			int ans2 = x[i].getlength();
			double ans1 = punchuationfeature(ans2);
			x[i].setpunchuation(ans1);
			x[i].makefeaturevector();

		}
		for (int i = 0; i < tweetcount; i++) {
			if (x[i].getpunchuation() > maxpunchw)
				maxpunchw = x[i].getpunchuation();
			if (x[i].getpunchuation() < minpunchw)
				minpunchw = x[i].getpunchuation();
		}
		System.out.println(maxw + "  " + maxnw + "  " + maxpw + "  "
				+ maxpunchw + " " + maxword);
		System.out.println(minw + "  " + minnw + "  " + minpw + "  "
				+ minpunchw + " " + minword);

		for (int i = 0; i < tweetcount; i++) {
			// System.out.println(x[i].getweight()+" "+x[i].getngramweight()+" "+x[i].getpatternweight()+" "+x[i].getpunchuation()+" "+x[i].getwordfeatureweight());
			x[i].setweight((x[i].getweight() - minw) / (double) (maxw - minw));
			x[i].setngramweight((x[i].getngramweight() - minnw)
					/ (double) (maxnw - minnw));
			// x[i].setpatternweight((x[i].getpatternweight()-minpw)/(double)(maxpw-minpw));
			x[i].setpunchuation((x[i].getpunchuation() - minpunchw)
					/ (double) (maxpunchw - minpunchw));
			x[i].setwordfeatureweight((x[i].getwordfeatureweight() - minword)
					/ (double) (maxword - minword));
			x[i].setpatternweight((x[i].getpatternweight()-minpw)/(double)(maxpw-minpw));
			
		}

		// System.out.println(tweetcount);
		// for(int i=0;i<tweetcount;i++)
		fileReader.close();
		// System.out.println("Contents of file:");
		// System.out.println(stringBuffer.toString());

		// System.out.println("loop end2");

		int sub = tweetcount / 5;

		for (int b = 0; b < 5; b++) {

			for (int i = 0; i < wordcnt; i++) {

				// listoflist.add(new ArrayList<Integer>());
				for (int j = 0; j < tweetcount; j++) {
					if (j == b * sub)
						j = j + sub;
					else {
						int found1 = 0;
						String tweet1 = x[j].gettweet();
						Scanner scanner = new Scanner(tweet1);
						while (scanner.hasNext()) {
							String nextToken = scanner.next();
							if (nextToken.equalsIgnoreCase(count[i])) {
								found1++;
								break;
							}
						}
						if (found1 != 0) {
							pair[i][j] = j;
							// listoflist.get(i).add(j);

						}
						// else
						// listoflist.get(i).add(0);
					}
				}

			}

			for (int i = 0; i < wordcnt; i++) {

				// listoflist.add(new ArrayList<Integer>());
				for (int j = b * sub; j < (sub * (b + 1)); j++) {

					int found1 = 0;
					String tweet1 = x[j].gettweet();
					// System.out.println(tweet1);
					Scanner scanner = new Scanner(tweet1);
					while (scanner.hasNext()) {
						String nextToken = scanner.next();
						if (nextToken.equalsIgnoreCase(count[i])) {
							found1++;
							break;
						}
					}
					if (found1 != 0) {
						pair[i][j] = j;
						// listoflist.get(i).add(j);

					}
					// else
					// listoflist.get(i).add(0);
				}

			}

			// System.out.println("loop end3");
			for (int i = 0; i < tweetcount; i++) {

				// listoflistfinal.add(new ArrayList<Integer>());
				for (int j = 0; j < wordcnt; j++) {
					if (pair[j][i] == i)
					// if(listoflist.get(j).get(i) == i)
					{
						for (int k = 0; k < tweetcount; k++) {
							// if(listoflist.get(j).get(k)!=0)
							if (pair[j][k] != 0) {
								finalpair[i][k] = 1;
								// listoflistfinal.get(i).add(k,1);
							}
							// else
							// listoflistfinal.get(i).add(k,0);

						}

					}
				}
				// System.out.println("\n");

			}
			/*
			 * for(int i=100;i<tweetcount;i++) { //int []arraypair=new
			 * int[tweetcount]; int j; //System.out.println("for "+i+"   ");
			 * for( j=0;j<tweetcount;j++) {
			 * //if(listoflistfinal.get(i).get(j)==1) if(finalpair[i][j]==1)
			 * {//System.out.print(j+" "); //arraypair[j]=j; } }
			 * 
			 * //x[j].setpair(arraypair); //System.out.println(); }
			 */

			for (int i = b * sub; i < (sub * (b + 1)); i++) {
				for (int j = 0; j < tweetcount; j++) {

					if (finalpair[i][j] == 1)
					// if( listoflistfinal.get(i).get(j)==1)
					{
						double x1 = Math.abs(x[i].getweight()
								- x[j].getweight());
						double y1 = Math.abs(x[i].getngramweight()
								- x[j].getngramweight());
						double z1 = Math.abs(x[i].getpunchuation()
								- x[j].getpunchuation());
						double k1 = Math.abs(x[i].getwordfeatureweight()
								- x[j].getwordfeatureweight());
						double k2=Math.abs(x[i].getpatternweight()-x[j].getpatternweight());
						// System.out.println(z1);

						euclid[i][j] = Math.sqrt(Math.abs((x1 * x1) + (y1 * y1)
								+ (z1 * z1) + (k1 * k1)+(k2*k2)));
						// euclid[i][j]=Math.sqrt(Math.abs)
						// System.out.print(euclid[i][j]+" ");

					} else
						euclid[i][j] = 99999;

				}
				// System.out.println("\n");
			}

			euclidpair[] values = new euclidpair[tweetcount];
			for (int p = 0; p < tweetcount; p++)
				values[p] = new euclidpair();

			int right = 0;
			int truepositive = 0;
			int truenegative = 0;
			int falsepositive = 0;
			int falsenegative = 0;
			int totalpositive = 0;
			int totalnegative = 0;

			for (int i = b * sub; i < (sub * (b + 1)); i++) {
				// double arrayforsort[]=new double[tweetcount];
				if (x[i].getlabel() == "positive")
					totalpositive++;
				else
					totalnegative++;

				for (int j = 0; j < tweetcount; j++) {
					// System.out.print(euclid[i][j]+" ");
					// arrayforsort[j]=euclid[i][j];
					values[j].setindex(j);
					// System.out.print(values[j].getindex()+" ");
					values[j].value = euclid[i][j];
					// System.out.print(values[j].value+" "+values[j].getindex());
				}
				// System.out.println("\n");

				euclidpair temp = new euclidpair();

				for (int k = 0; k < tweetcount - 1; k++) {
					for (int j = 1; j < tweetcount - 1; j++) {
						if (values[j].value > values[j + 1].value) {
							temp = values[j];
							values[j] = values[j + 1];
							values[j + 1] = temp;
							// System.out.println(temp.value+" "+values[j+1].value);

						}

					}
				}

				int pos = 0;
				int neg = 0;
				int neu = 0;

				int k;

				for (k = 0; k < 8; k++) {
					// System.out.println(x[values[k].getindex()]);
					int temp10 = values[k].getindex();
					// System.out.println(temp10);
					if (x[temp10].getsent().equalsIgnoreCase("positive"))
						pos++;
					else if (x[temp10].getsent().equalsIgnoreCase("negative"))
						neg++;
					// else
					// if(x[values[k].getindex()].getsent().equalsIgnoreCase("neutral"))
					// neu++;

				}
				// if(neu>pos && neu>neg)
				// {

				// System.out.println
				// (x[i].getlabel()+" "+"neutral"+pos+" "+neg+" "+neu);
				// if(x[i].getlabel()=="neutral")

				// }
				// System.out.println(pos+" "+neg);
				if (pos >= neg) {

					// System.out.println
					// (x[i].getlabel()+" "+"positive"+pos+" "+neg+" "+neu);
					if (x[i].getlabel() == "positive") {
						// System.out.println("------------positive");
						truepositive++;
					} else
						falsepositive++;

				} else {
					// System.out.println
					// (x[i].getlabel()+" "+"negative"+pos+" "+neg+" "+neu);
					if (x[i].getlabel() == "negative") {
						// System.out.println("------------negative");
						right++;
						truenegative++;
					} else
						falsenegative++;

				}

			}
			// System.out.println("loop end4");
			// System.out.println("True positive:"+positiveright);
			// System.out.println("False positive:"+falsepositive);
			// System.out.println("True negative:"+negativeright);
			// System.out.println("False negative:"+falsenegative);
			// System.out.println("Total Tweets:"+tweetcount);
			double tempprecession = ((truepositive) / (double) (truepositive + falsepositive));
			double temprecall = ((truepositive) / (double) (totalpositive));
			double tempaccuracy = ((truepositive + truenegative) / (double) ((totalpositive + totalnegative))) * 100;
			double tempfscore = (2 / (double) ((1 / tempprecession) + (1 / temprecall)));
			finalprecession = finalprecession + tempprecession;
			finalrecall = finalrecall + temprecall;
			final_accuracy = final_accuracy + tempaccuracy;
			finalfscore = finalfscore + tempfscore;
			//System.out.println("precession of fold" + (b + 1) + ":"
				//	+ tempprecession);
		//	System.out.println("recall of fold" + (b + 1) + ":" + temprecall);
		//	System.out.println("Fscore of fold" + (b + 1) + ":" + tempfscore);
		//	System.out.println("Accuracy of fold" + (b + 1) + " :"
					//+ tempaccuracy);
			
		
			
			//System.out.println("*************");
			

			// for(int i=0;i<tweetcount;i++)
			// {
			// System.out.println(x[i].getngramweight()+" "+x[i].getweight()+" "+x[i].getlabel());
			// if(x[i].getlabel().contains("positive"))
			// System.out.println(1+" "+"1#"+x[i].getngramweight()+" "+"2#"+x[i].getweight());
			// else
			// System.out.println(-1+" "+"1#"+x[i].getngramweight()+" "+"2#"+x[i].getweight());
			// System.out.println("total"+i+"\n");
			// System.out.println(i);
			// x[i].display();
			// }
			// System.out.println(tweetcount);
			// System.out.println(right+" "+positiveright+" "+negativeright+" "+tweetcount+" "+wordcnt+" "+countngram);
		}
		System.out.println("Avg precession:" + (finalprecession / 5.0));
		System.out.println("Avg recall:" + (finalrecall / 5.0));
		System.out.println("Avg FScore:" + (finalfscore / 5.0));
		System.out.println("Avg accuracy:" + (final_accuracy / 5.0));
		
		for(int i=0;i<tweetcount;i++)
			System.out.println(x[i].getpatternweight());

		for (int i = 0; i < tweetcount; i++) {
			// System.out.println(x[i].getngramweight()+" "+x[i].getweight()+" "+x[i].getpunchuation()+" "+x[i].getwordfeatureweight());
			// if (x[i].getlabel().contains("positive")) {//
			// parameter[i]=(1+" "+"1#"+x[i].getngramweight()+" "+"2#"+x[i].getweight()+" "+"3#"+x[i].getpunchuation()+"4#"+x[i].getpatternweight()+"\n");
			// parameter[i] = (1 + " " + "1#" + x[i].getngramweight() + " "
			// + "2#" + x[i].getweight() + " " + "3#"
			// + x[i].getpunchuation()
			// +" "+"4#"+x[i].getwordfeatureweight()+"\n");
			// actual_level[i]=1;
			// } else {//
			// parameter[i]=(-1+" "+"1#"+x[i].getngramweight()+" "+"2#"+x[i].getweight()+" "+"3#"+x[i].getpunchuation()+"4#"+x[i].getpatternweight()+"\n");
			// parameter[i] = (-1 + " " + "1#" + x[i].getngramweight() + " "
			// + "2#" + x[i].getweight() + " " + "3#"
			// + x[i].getpunchuation() +" "+"4#"+x[i].getwordfeatureweight()+
			// "\n");
			// actual_level[i]=-1;
			// }

			pcaparameter[i][0] = x[i].getngramweight();
			pcaparameter[i][1] = x[i].getweight();
			pcaparameter[i][2] = x[i].getpunchuation();
			pcaparameter[i][3] = x[i].getwordfeatureweight();
			 
			 if(x[i].getpatternweight()== Double.NaN)
			pcaparameter[i][4]=1.0;
			 else
				 pcaparameter[i][4]=x[i].getpatternweight();
			// System.out.println(pcaparameter[i][3]);

			// System.out.println("total"+i+"\n");
			// System.out.println(i);
			// x[i].display();
		}
		for(int i=0;i<tweetcount;i++)
			System.out.println(pcaparameter[i][4]);
		
         System.out.println("berfore pca");
		PCA object = new PCA();
		object.inputdata(pcaparameter, tweetcount, 5);

		pcaparameter = object.gettransformeddata();
		
		   System.out.println("after pca");
		
		
		
		
		
		
		
		
		
		
		FileWriter fw = new FileWriter("data1.data");
		for (int i = 0; i < tweetcount; i++) {
			// System.out.println(x[i].getngramweight()+" "+x[i].getweight()+" "+x[i].getlabel());
			if (x[i].getlabel().contains("positive")) {// parameter[i]=(1+" "+"1#"+pcaparameter[i][0]+" "+"2#"+pcaparameter[i][1]+" "+"3#"+pcaparameter[i][2]+"4#"+pcaparameter[i][3]+"\n");
				fw.write(pcaparameter[i][0] + ","+ pcaparameter[i][1] + "," +
						+ pcaparameter[i][2] + ","+ pcaparameter[i][3] +","+ pcaparameter[i][4] +","+1+"\n");
				parameter[i] = (1 + " " + "1#" + pcaparameter[i][0] + " "
						+ "2#" + pcaparameter[i][1] + " " + "3#"
						+ pcaparameter[i][2] + " " + "4#" + pcaparameter[i][3] +" "+"5"+pcaparameter[i][4] +"\n");// actual_level[i]=1;
			} else {// parameter[i]=(-1+" "+"1#"+pcaparameter[i][0]+" "+"2#"+pcaparameter[i][1]+" "+"3#"+pcaparameter[i][2]+"4#"+pcaparameter[i][3]+"\n");
				
				fw.write(pcaparameter[i][0] + ","+ pcaparameter[i][1] + "," +
						+ pcaparameter[i][2] + ","+ pcaparameter[i][3] +","+ pcaparameter[i][4] +","+ -1+"\n");
				
				
				parameter[i] = (-1 + " " + "1#" + pcaparameter[i][0] + " "
						+ "2#" + pcaparameter[i][1] + " " + "3#"
						+ pcaparameter[i][2] + " " + "4#" + pcaparameter[i][3] +" "+"5"+ pcaparameter[i][4] +"\n");
				// actual_level[i]=-1;
			}
			// System.out.println(parameter[i]);

			// pcaparameter[i][0]=x[i].getngramweight();
			// pcaparameter[i][1]=x[i].getweight();
			// pcaparameter[i][2]=x[i].getpunchuation();
			// System.out.println(pcaparameter[i][0]+"  "+pcaparameter[i][1]+" "+pcaparameter[i][2]);
			// System.out.println("total"+i+"\n");
			// System.out.println(i);
			// x[i].display();
			
			
			
		}
		fw.close();
		
		
		
		
		
		 Dataset data = FileHandler.loadDataset(new File("data1.data"), 5, ",");
			svm_parameter param = new svm_parameter();
			// default values
			param.svm_type = svm_parameter.C_SVC;
			param.kernel_type = svm_parameter.RBF;
			param.degree = 3;
			param.gamma = 0.5;	
			param.coef0 = 0;
			param.nu = 0.5;
			param.cache_size = 2000;
			param.C = 1;
			param.eps = 1e-3;
			param.p = 0.1;
			param.shrinking = 1;
			param.probability = 1;
			param.nr_weight = 0;
			param.weight_label = new int[0];
			param.weight = new double[0];
			LibSVM classifier=new LibSVM();
		
			GridSearch calc = new GridSearch(classifier,data,5);
		
		
		
		
		
		
		
		
		
		
		
		
		

		/*
		 * for(int i=0;i<tweetcount;i++) {
		 * if(x[i].getlabel().contains("positive")) System.out.println(1); else
		 * System.out.println(-1);
		 * 
		 * }
		 */
		
		
		
		
		
		
		
		
		
		
		
		
		
	System.out.println("before svm");

		support_vector_machine t123 = new support_vector_machine();

		for (int i = 0; i < 5; i++) {
			
		int k = 0, j,q=0;

			for (j = 0; j < tweetcount; j++) {
				
				if (j == i * sub)
				{
					for( int z=0;z<sub;z++)
					{
						parameter2[z]=parameter[j];
					j++;
					
					}
					
					
				}
				else
					parameter1[k++] = parameter[j];
			}
			
			
			for(int p=0;p<q;p++)
				System.out.println(parameter2[p]);
			
			svm_model model2 = t123.svmTrain(parameter1, k, 5,param.C,param.gamma);
			// t123.sendlevel(actual_level,tweetcount);

		//	k = 0;
		//	for (j = i * sub; j < (sub * (i + 1)); j++, k++)
			//	parameter1[k] = parameter[j];

			// t123.sendlevel(actual_level);

			t123.evaluate_all_instances(parameter2, model2, sub);
			t123.getaccuracy(sub, i+1);
			}
		
	t123.getfinalaccuracy(tweetcount);// System.out.println(tweetcount);
		// for(int i=0;i<tweetcount;i++)
		// System.out.println(x[i].gettweet());
	
		// }

	}

}
