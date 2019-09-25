package tree;

public class TreeNode {
	public TreeNode left;
	public TreeNode middle;
	public TreeNode right;

	private String label;
	private String prompt;
	private String message;

	public TreeNode() {

	}

	public TreeNode(String label, String prompt, String message) {
		this.label = label;
		this.prompt = prompt;
		this.message = message;
	}

	public TreeNode getLeft() {
		return this.left;
	}

	public void setLeft(TreeNode left) {
		this.left = left;
	}

	public void setMiddle(TreeNode middle) {
		this.middle = middle;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}

	public TreeNode getMiddle() {
		return this.middle;
	}

	public TreeNode getRight() {
		return this.right;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPrompt() {
		return this.prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLeaf(TreeNode node) {
		if (node.getLeft() == null) {
			return true;
		}
		return false;
	}
}
