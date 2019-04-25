import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class BinaryTree{
	public Node root = null;
	public int numberOfWord = 0;
	
	public void init() throws IOException {
		readFile();
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("$ : ");
			String line = sc.nextLine();
			String[] command = line.split(" ");
			
			if(command[0].compareTo("add")==0) {
				System.out.print("word : "); String word = sc.nextLine();
				System.out.print("class : "); String wordClass = sc.nextLine();
				System.out.print("meaning : "); String meaning = sc.nextLine();
				insert(new Node(word,wordClass,meaning));
			}
			else if(command[0].compareTo("find")==0) {
				search(command[1]);
			}
			else if(command[0].compareTo("delete")==0) {
				String deleteWord = command[1];
				ArrayList<Node>deleteList = findWordList(deleteWord);
				int deleteWordCount = deleteNode(deleteList);
				if(deleteWordCount != 0)
					System.out.println("Delete Successfully.");
				else
					System.out.println("Not found word.");
			}
			else if(command[0].compareTo("size")==0) {
				System.out.println("The number of word : " + numberOfWord);
				continue;
			}else{
				int deleteWordCount = deleteAllNode();
				System.out.println(deleteWordCount + " words were deleted successfully.");
			}
		}
	}
	private void readFile() throws IOException {
		File input = new File("shuffled_dict.txt");
		BufferedReader br = new BufferedReader(new FileReader(input));
		String line;
		line = br.readLine();
		while((line = br.readLine())!=null) {
			int wordIndex = line.indexOf("(");
			int classIndex = line.indexOf(")");
			int meaningIndex = line.indexOf("\n");
			String new_word = line.substring(0, wordIndex-1);
			String new_class = line.substring(wordIndex+1, classIndex);
			String new_meaning = line.substring(classIndex+1, line.length());
			insert(new Node(new_word,new_class,new_meaning));
		}
	}
	private ArrayList<Node> readDeleteWordFile() throws IOException{
		File input = new File("to_be_deleted_words.txt");
		BufferedReader br = new BufferedReader(new FileReader(input));
		String deleteWord;
		ArrayList<Node> deleteList = new ArrayList();
		while((deleteWord = br.readLine())!=null){
			deleteList.add(new Node(deleteWord,"",""));
		}
		return deleteList;
	}
	private void insert(Node newNode) {
		Node x = root;
		Node y = null;

		while(x != null) {
			y = x;
			if(newNode.word.compareToIgnoreCase(x.word)<0)
				x = x.left;
			else
				x = x.right;
		}
		if(y == null)
			root = newNode;
		else if(y.word.compareToIgnoreCase(newNode.word)<=0){
			x = newNode; y.right = x; x.parent = y;
		}else { x = newNode; y.left =x; x.parent = y; }
		numberOfWord++;	
	}
	
	private void search(String findTargetWord) {
		if(findTargetWord.compareTo("")==0)
			return;
		ArrayList<Node> wordList = findWordList(findTargetWord);
		print(wordList);
	}

	private ArrayList<Node> findWordList(String targetWord){
		ArrayList<Node> wordList = new ArrayList<Node>();
		Node cursor = root;
		
		while(cursor != null) {
			if(cursor.word.compareToIgnoreCase(targetWord)==0) {
				wordList.add(cursor); break;
			}
			else if(cursor.word.compareToIgnoreCase(targetWord)<=0) 
				cursor = cursor.right;
			else
				cursor = cursor.left;
		}
		return wordList;
	}
	
	private void print(ArrayList<Node> wordList) {
		for(int i=0; i<wordList.size(); i++)
			System.out.println("meaning : " + wordList.get(i).meaning);
		if(wordList.size() ==0)
			System.out.println("Not found word.");
	}
	
	
	private Node treeMinimum(Node NextMaxToTargetNode) {
		while(NextMaxToTargetNode.left !=null)
			NextMaxToTargetNode = NextMaxToTargetNode.left;
		return NextMaxToTargetNode;
	}
	
	private Node findSuccessorNode(Node targetNode) {
		if(targetNode.right !=null)
			return treeMinimum(targetNode);
		Node ParentNode = targetNode.parent;
		while(ParentNode !=null && targetNode == ParentNode.right) {
			targetNode = ParentNode;
			ParentNode = ParentNode.parent;
		}
		return ParentNode;
	}
	
	private int deleteNode(ArrayList<Node> deleteList) {
		int count=0;

		for(int i=0; i<deleteList.size();i++) {
			Node x = root;
			Node y = null;
			Node z = deleteList.get(i);
			if(z.left == null || z.right == null)
				y = z;
			else 
				y = findSuccessorNode(z);
			if(y.left != null)
				x = y.left;
			else x = y.right;
			if(x!=null)
				x.parent = y.parent;
			else if(y.parent != null && y == y.parent.left )
				y.parent.left = x;
			else if(y.parent != null)
				y.parent.right = x;
			if(y !=z)
				{z.word = y.word; z.wordClass = y.wordClass; z.meaning = y.meaning;}
			count++;
			numberOfWord--;
		}
		return count;
	}
	private int deleteAllNode() throws IOException {
		ArrayList<Node> deleteWordList = readDeleteWordFile();
		int count = deleteNode(deleteWordList);
		return count;
	}
}