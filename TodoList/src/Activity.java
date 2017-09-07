
public class Activity {
	private int time;
	private String name;
	
	public Activity(String n) {
		this.time = 0;
		this.name = n;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void increment() {
		time = time + 1;
	}

}
