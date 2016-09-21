package Part2.threads;

public  class Shared0 {
	protected int x=0, y=0;
	synchronized public int dif(){
		return x-y;
	}
	synchronized public void bump() throws InterruptedException{
		x++;
		Thread.sleep(9);
		y++;
	}
}
