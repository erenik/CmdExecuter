package Part4;

// Interface, very cool, to link together services into the MessageServer easily.
public interface Deliverable {
	Message send (Message m);
}

