package cn.domoe.DIDS;

public class DIDS {

	private Transaction[] Du = null;
	private double r = 0;
	private int MinPts = 0;
	public DIDS(Transaction[] Du , double r , int MinPts) {
		this.Du = Du;
		this.r = r;
		this.MinPts = MinPts;
		
		this.tenFoldCrossValidation();
		OPTICS opticsProFu = new OPTICS(this.Train,r,MinPts);
		Item[] proFu = opticsProFu.getResult();
		
		OPTICS opticsRd = new OPTICS(this.Test,r,MinPts);
		Item[] rd = opticsRd.getResult();
		
		double of = OFs.OF();
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
