package tree;

import java.util.*;

public class Tree {
	TreeNode root;
	

	public boolean addNode(String label, String prompt, String message, String parentLabel) {
		TreeNode node = null;
		TreeNode cursor = null;

		if (root == null) {
			root = new TreeNode(label, prompt, message);
			return true;
		} else {
			cursor = root;

		}

	}

	public TreeNode getNodeReference(String label) {
		TreeNode cursor = root;
		
		if (){
			
		}
	}

	public void preOrder() {
		if (root == null)
			throw new EmptyTreeException("Empty Tree");	
		
		node = root;
		System.out.println(node.getLabel()+"\n"+node.getPrompt()+"\n"+node.getMessage());
		if (root.left != null)
			root.left.preorder();
		if (middle != null)
			middle.
		if (right != null)
			right.preorder();
	}

	public void beginSession() {

		System.out.println(node.getMessage());
	}
}