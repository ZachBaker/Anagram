
public class DLB implements DictInterface{
	
	Node DLBStart;
	
	public DLB(){
		DLBStart = null;
	}
	
	public boolean add(String S){
		Node currNode;
		
		//If the DLB is empty creates a link list of children followed by a Node with a sentinel character. Sets the root to the first character
		//of this word.
		
		if(DLBStart == null){
			Node tempNode = new Node(S.charAt(0),null,null);
			DLBStart = tempNode;
			
			for(int i = 1; i<S.length(); i++){
				currNode = new Node(S.charAt(i),null,null);
				tempNode.child = currNode;
				tempNode = currNode;
			}
			currNode = new Node('^', null, null);
			tempNode.child = currNode;
			
			return true;
		}
		
		//If DLB is not empty, then start following the path of the given DLB until it ends. Once it ends
		//add the new word.
		
		else if(DLBStart != null){
			currNode = DLBStart;
			int i = 0;
			
			while(currNode != null && i < S.length()){
				if(currNode.data == S.charAt(i)){
					if(currNode.child != null){
						currNode = currNode.child;
						i++;
					}
					
					else{
						Node tempNode = new Node(S.charAt(i),null,null);
						currNode.child = tempNode;
						return(addFinish(tempNode, S, i+1));
					}
				}
				
				else{
					if(currNode.sibling != null)
						currNode = currNode.sibling;
					
					
					else if (currNode.sibling == null){
						currNode.sibling = new Node(S.charAt(i),null,null);
						currNode = currNode.sibling;
						return(addFinish(currNode,S,i+1));
					}
				}
			}
		}
		return true;
	}
		
	//If the DLB already has data in it, performs an algorithm to add the new word in. This includes trying to find each character in the 
	//new word in the DLB. Once a character cannot be found by traversing through the siblings on that level, a new sibling is added on 
	//that level and a linked list of characters is created with the remaining characters as each others children. 
		
	
	public boolean addFinish(Node currNode, String S, int pos)
	{
		for(int i = pos; i<S.length(); i++){
			Node tempNode = new Node(S.charAt(i),null,null);
			currNode.child = tempNode;
			currNode = tempNode;
		}
		
		Node tempNode = new Node('^',null,null);
		currNode.child = tempNode;
		
		return true;
	}
	
	public int searchPrefix(StringBuilder s){
		boolean prefix = false;
		boolean word = false;
		
		int i = 0;
		
		Node currNode = DLBStart;
		
		while(currNode != null && i < s.length()){
			if(currNode.data == s.charAt(i)){
				currNode = currNode.child;
				i++;
			}
			
			else if(currNode.data != s.charAt(i))
				currNode = currNode.sibling;
		}
		
		if(currNode == null){
			prefix = false; 
			word = false;
		}
		
		else if(currNode != null){
		
			while(currNode != null){
				if(currNode.data == '^')
					word = true;

				if(currNode.data != '^')
					prefix = true;

				currNode = currNode.sibling;

			}
		}
			
		
		if(word && prefix)
			return 3;
		else if(word)
			return 2;
		else if (prefix)
			return 1;
		else
			return 0;
			
	}
	
	//Pretty much the same as the first searchPrefix, except use start as i instead of 0 and end
	//instead of s.length()
	
	public int searchPrefix(StringBuilder s, int start, int end){

		boolean prefix = false;
		boolean word = false;

		int i = start;

		Node currNode = DLBStart;
		
		while(currNode != null && i <= end){
			if(currNode.data == s.charAt(i)){
				currNode = currNode.child;
				i++;
			}
			
			else if(currNode.data != s.charAt(i))
			{
				currNode = currNode.sibling;
			}
		}
		
		if(currNode == null){
			prefix = false; 
			word = false;
		}
		
		else if(currNode != null){
			while(currNode != null){
				if(currNode.data == '^')
					word = true;

				if(currNode.data != '^')
					prefix = true;

				currNode = currNode.sibling;
			}
		}

		if(word && prefix)
			return 3;
		else if(word)
			return 2;
		else if (prefix)
			return 1;
		else
			return 0;
	}
	
	private class Node{
		
		private char data;
		private Node sibling;
		private Node child;
		
		 public Node(char data, Node sibling, Node child){
			 this.data = data;
			 this.sibling = sibling;
			 this.child = child;
		 }
	}
}
