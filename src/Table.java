/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/



// line 1 "RestoAppTableStateMachine.ump"
public class Table
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Table State Machines
  public enum Status { Available, NothingOrdered, Ordered }
  private Status status;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Table()
  {
    setStatus(Status.Available);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean startOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Available:
        // line 4 "RestoAppTableStateMachine.ump"
        new Order(new java.sql.Date(Calendar.getInstance().getTime().getTime()), this.getRestoApp().getWaiter(0), new java.sql.Time(Calendar.getInstance().getTime().getTime()), this.getRestoApp(), this);
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToOrder(Order o)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Available:
        // line 7 "RestoAppTableStateMachine.ump"
        o.addTable(this);
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean orderItem(int quantity,Order o,Seat s,PricedMenuItem i)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        if (quantityNotNegative(quantity))
        {
        // line 12 "RestoAppTableStateMachine.ump"
          // create a new order item with the provided quantity, order, seat, and priced menu item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      case Ordered:
        if (quantityNotNegative(quantity))
        {
        // line 32 "RestoAppTableStateMachine.ump"
          // create a new order item with the provided quantity, order, seat, and priced menu item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToOrderItem(OrderItem i,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        // line 17 "RestoAppTableStateMachine.ump"
        List<Seat> seats = i.getSeats();
			if(!seats.contains(s)) {
				i.addSeat(s);		
				}
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      case Ordered:
        // line 35 "RestoAppTableStateMachine.ump"
        // add provided seat to provided order item unless seat has already been added, in which case nothing needs to be done
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean endOrder(Order o)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        // line 23 "RestoAppTableStateMachine.ump"
        if (!o.removeTable(this)) {
               if (o.numberOfTables() == 1) {
                  o.delete();
               }
            }
        setStatus(Status.Available);
        wasEventProcessed = true;
        break;
      case Ordered:
        if (allSeatsBilled())
        {
        // line 57 "RestoAppTableStateMachine.ump"
          
          setStatus(Status.Available);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelOrderItem(OrderItem i)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        if (iIsLastItem(i))
        {
        // line 38 "RestoAppTableStateMachine.ump"
          // delete order item
          setStatus(Status.NothingOrdered);
          wasEventProcessed = true;
          break;
        }
        if (!(iIsLastItem(i)))
        {
        // line 41 "RestoAppTableStateMachine.ump"
          // delete order item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 44 "RestoAppTableStateMachine.ump"
        // delete all order items of the table
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean billForSeat(Order o,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 47 "RestoAppTableStateMachine.ump"
        // create a new bill with the provided order and seat; if the provided seat is already assigned to
            // another bill for the current order, then the seat is first removed from the other bill and if no seats
            // are left for the bill, the bill is deleted
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToBill(Bill b,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 52 "RestoAppTableStateMachine.ump"
        // add provided seat to provided bill unless seat has already been added, in which case nothing needs
            // to be done; if the provided seat is already assigned to another bill for the current order, then the
            // seat is first removed from the other bill and if no seats are left for the bill, the bill is deleted
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;
  }

  public void delete()
  {}


  /**
   * check that the provided quantity is an integer greater than 0
   */
  // line 64 "RestoAppTableStateMachine.ump"
   private boolean quantityNotNegative(int quantity){
    if (quantity > 0 ){
      return true;
      }
      return false;
  }


  /**
   * check that the provided order item is the last item of the current order of the table
   */
  // line 72 "RestoAppTableStateMachine.ump"
   private boolean iIsLastItem(OrderItem i){
    // TODO
      
      Order order = i.getOrder();
      List <OrderItem> orderItems= order.getOrderItems();
      int size= orderItems.size();
      if(size==1 && orderItems.get(0)==i){
      return true;
      }
      
   return false;
  }

  // line 86 "RestoAppTableStateMachine.ump"
   private boolean allSeatsBilled(){
    List<Seat> curSeats = getCurrentSeats(); // get current seats in the whole restaurant
		List<Seat> activeSeats = new ArrayList<Seat>(); // this will be used later to store all active seats at table
		// have to move current seats into list that is modifiable
		List<Seat> tempSeats = new ArrayList< Seat>();
		for( Seat seat: curSeats){
		tempSeats.add(seat);
		}
		
	
		//this section is used to get the last order in the current orders for the table
		List<Order> orders = getOrders();
		int listSize = orders.size();
		Order lastOrder;
		if (listSize == 0) {
			return false;
		} else {
			listSize = listSize - 1;
			lastOrder = orders.get(listSize);
		}
		
		List<Bill> curBills = lastOrder.getBills();// get bills for this order in this table. These are the bills that the seats must have
		List<OrderItem> curOrderItems = lastOrder.getOrderItems();// get order items for this order at this table

		// get all seats that have order item
		for (OrderItem item : curOrderItems) {

			List<Seat> tempList = item.getSeats();
			for (Seat seat : tempList) {
				activeSeats.add(seat);
			}
		}
		// remove seats from all current seats that do not have an order item

		for (Seat seat : tempSeats) {
			if (!activeSeats.contains(seat)) {
				tempSeats.remove(seat);
			}
		}
		// all seats in cur seats are current and have order items 
		// check logic
		int i = 0;
		for (Seat seat : tempSeats) {
			List<Bill> billsOfSeat = seat.getBills();
			for (Bill bill : curBills) {
				if (billsOfSeat.contains(bill)) {
					i += 1;
				}
			}
			if (i == 0) {
				return false;
			}
			i = 0;
		}
		return true;
  }


  /**
   * line 37 "../../../../../RestoApp v3.ump"
   */
  // line 144 "RestoAppTableStateMachine.ump"
   public boolean doesOverlap(int x2, int y2, int width2, int length2){
    return x2 < this.x + this.width && x2 + width2 > this.x && y2 < this.y + this.length && y2 + length2 > this.y;
  }

}