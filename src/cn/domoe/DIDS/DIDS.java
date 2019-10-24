package cn.domoe.DIDS;

public class DIDS {

	/*
	 * 计算基于r、Minpts、k的核心距离
	 * 
	 * return
	 * null 无穷
	 * double
	 * */
	public static void cd(double r,int Minpts,Object k ,Object dataset) {
		
	}
	
	public static void rd(double r,int Minpts,Object k,Object q,Object dataset) {
		
	}
	
	public static void lrd(double a) {
		
		
	}

	private Transaction[] Du = null;
	private double r = 0;
	private int MinPts = 0;
	public DIDS(Transaction[] Du , double r , int MinPts) {
		this.Du = Du;
		this.r = r;
		this.MinPts = MinPts;
		
		this.tenFoldCrossValidation();
		OPTICS ProFu = new OPTICS(this.Train,r,MinPts);
		OPTICS rd = new OPTICS(this.Test,r,MinPts);
		OFs ofs = new OFs(rd);
	}
	
	
	private Transaction[] Test = null;
	private Transaction[] Train = null;
	
	private void tenFoldCrossValidation() {
		int testNum = this.Du.length / 10;
		Test = new Transaction[testNum];
		Train = new Transaction[this.Du.length - testNum];
		int i = 0;
		for(; i < testNum ; i++) {
			Test[i] = this.Du[i];
		}
		for(int j = 0; j < Train.length;j++) {
			Train[i] = this.Du[j++];
		}
	}

	
	
	public static void main(String[] args) {
		
	}
}
