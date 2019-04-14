import java.io.*;

public class BPlusTreeFunctions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File(args[0]);
		String st;
		BufferedReader br = null;
		BufferedWriter wr = null;
		try 
		{
			br = new BufferedReader(new FileReader(file));
			wr = new BufferedWriter(new FileWriter("output.txt"));
			String init = br.readLine();
			//wr = new PrintWriter("output_file.txt");
			BPlusTree bTree = new BPlusTree(Integer.parseInt(init.substring(init.indexOf('(')+1, init.indexOf(')'))));
			while ((st = br.readLine()) != null)
			{
				String strIns, strVal, output=null;
				int key, key1, key2;
				double value;
				//System.out.println(st);
				strIns = st.substring(0, st.indexOf('('));
				strIns = strIns.trim().toLowerCase();				
				strVal = st.substring(st.indexOf('(')+1, st.indexOf(')'));
				System.out.println(strIns + ":" + strVal);
				String []a = strVal.split(",");
				switch (strIns)
				{
				case "insert":
					key = Integer.parseInt(a[0].trim());
					value = Double.parseDouble(a[1].trim());
					bTree.insertInTree(key, value);
					break;
				case "delete":
					key = Integer.parseInt(strVal);
					bTree.deleteNode(key);
					break;
				case "search":
					if(strVal.contains(","))
					{
						key1 = Integer.parseInt(a[0].trim());
						key2 = Integer.parseInt(a[1].trim());
						output = bTree.searchInTree(key1,key2);
					}
					else
					{
						key = Integer.parseInt(a[0]);
						output = bTree.searchInTree(key);
					}
					break;					
				}	
				if(output != null)
				{
					output = output + "\n";
					wr.write(output);
				}
			}
			wr.close();
		} 
		catch (FileNotFoundException e1) 
		{
			System.out.println("No file found at path: "+file.getAbsolutePath());
		} 
		catch (IOException e) 
		{
			System.out.println("Error: "+ e.getMessage());
		}
	}

}
