
public class Node {
	public String word;
	public String wordClass;
	public String meaning;
	public Node parent;
	public Node left;
	public Node right;
	
	public Node(String w, String wC, String m) {
		this.word = w; this.wordClass = wC; this.meaning = m;
	}
}