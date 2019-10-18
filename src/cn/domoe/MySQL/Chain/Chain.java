package cn.domoe.MySQL.Chain;

public abstract class Chain {
	
	protected String sql = null;
	protected String el = "chain";
	
	public Chain(String sql,String el) {
		this.sql = sql;
		this.el = el;
	}
	
	public String getEl(){
		return this.el;
	}


	public abstract void print();
	
	public abstract Chain[] nextChain() throws Exception;

	public abstract Object getValue();
	
}
