/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 36 "iteration1.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private String date;
  private String time;

  //Order Associations
  private List<OrderItem> orderItems;
  private List<FoodItem> foodItems;
  private List<Bill> bills;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(String aDate, String aTime)
  {
    date = aDate;
    time = aTime;
    orderItems = new ArrayList<OrderItem>();
    foodItems = new ArrayList<FoodItem>();
    bills = new ArrayList<Bill>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(String aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(String aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public String getDate()
  {
    return date;
  }

  public String getTime()
  {
    return time;
  }

  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }

  public FoodItem getFoodItem(int index)
  {
    FoodItem aFoodItem = foodItems.get(index);
    return aFoodItem;
  }

  public List<FoodItem> getFoodItems()
  {
    List<FoodItem> newFoodItems = Collections.unmodifiableList(foodItems);
    return newFoodItems;
  }

  public int numberOfFoodItems()
  {
    int number = foodItems.size();
    return number;
  }

  public boolean hasFoodItems()
  {
    boolean has = foodItems.size() > 0;
    return has;
  }

  public int indexOfFoodItem(FoodItem aFoodItem)
  {
    int index = foodItems.indexOf(aFoodItem);
    return index;
  }

  public Bill getBill(int index)
  {
    Bill aBill = bills.get(index);
    return aBill;
  }

  public List<Bill> getBills()
  {
    List<Bill> newBills = Collections.unmodifiableList(bills);
    return newBills;
  }

  public int numberOfBills()
  {
    int number = bills.size();
    return number;
  }

  public boolean hasBills()
  {
    boolean has = bills.size() > 0;
    return has;
  }

  public int indexOfBill(Bill aBill)
  {
    int index = bills.indexOf(aBill);
    return index;
  }

  public boolean isNumberOfOrderItemsValid()
  {
    boolean isValid = numberOfOrderItems() >= minimumNumberOfOrderItems();
    return isValid;
  }

  public static int minimumNumberOfOrderItems()
  {
    return 1;
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    orderItems.add(aOrderItem);
    if (aOrderItem.indexOfOrder(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrderItem.addOrder(this);
      if (!wasAdded)
      {
        orderItems.remove(aOrderItem);
      }
    }
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    if (!orderItems.contains(aOrderItem))
    {
      return wasRemoved;
    }

    if (numberOfOrderItems() <= minimumNumberOfOrderItems())
    {
      return wasRemoved;
    }

    int oldIndex = orderItems.indexOf(aOrderItem);
    orderItems.remove(oldIndex);
    if (aOrderItem.indexOfOrder(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrderItem.removeOrder(this);
      if (!wasRemoved)
      {
        orderItems.add(oldIndex,aOrderItem);
      }
    }
    return wasRemoved;
  }

  public boolean setOrderItems(OrderItem... newOrderItems)
  {
    boolean wasSet = false;
    ArrayList<OrderItem> verifiedOrderItems = new ArrayList<OrderItem>();
    for (OrderItem aOrderItem : newOrderItems)
    {
      if (verifiedOrderItems.contains(aOrderItem))
      {
        continue;
      }
      verifiedOrderItems.add(aOrderItem);
    }

    if (verifiedOrderItems.size() != newOrderItems.length || verifiedOrderItems.size() < minimumNumberOfOrderItems())
    {
      return wasSet;
    }

    ArrayList<OrderItem> oldOrderItems = new ArrayList<OrderItem>(orderItems);
    orderItems.clear();
    for (OrderItem aNewOrderItem : verifiedOrderItems)
    {
      orderItems.add(aNewOrderItem);
      if (oldOrderItems.contains(aNewOrderItem))
      {
        oldOrderItems.remove(aNewOrderItem);
      }
      else
      {
        aNewOrderItem.addOrder(this);
      }
    }

    for (OrderItem anOldOrderItem : oldOrderItems)
    {
      anOldOrderItem.removeOrder(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }

  public boolean isNumberOfFoodItemsValid()
  {
    boolean isValid = numberOfFoodItems() >= minimumNumberOfFoodItems();
    return isValid;
  }

  public static int minimumNumberOfFoodItems()
  {
    return 1;
  }

  public FoodItem addFoodItem(String aItem, int aPrice, Menu aMenu)
  {
    FoodItem aNewFoodItem = new FoodItem(aItem, aPrice, this, aMenu);
    return aNewFoodItem;
  }

  public boolean addFoodItem(FoodItem aFoodItem)
  {
    boolean wasAdded = false;
    if (foodItems.contains(aFoodItem)) { return false; }
    Order existingOrder = aFoodItem.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);

    if (isNewOrder && existingOrder.numberOfFoodItems() <= minimumNumberOfFoodItems())
    {
      return wasAdded;
    }
    if (isNewOrder)
    {
      aFoodItem.setOrder(this);
    }
    else
    {
      foodItems.add(aFoodItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeFoodItem(FoodItem aFoodItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aFoodItem, as it must always have a order
    if (this.equals(aFoodItem.getOrder()))
    {
      return wasRemoved;
    }

    //order already at minimum (1)
    if (numberOfFoodItems() <= minimumNumberOfFoodItems())
    {
      return wasRemoved;
    }

    foodItems.remove(aFoodItem);
    wasRemoved = true;
    return wasRemoved;
  }

  public boolean addFoodItemAt(FoodItem aFoodItem, int index)
  {  
    boolean wasAdded = false;
    if(addFoodItem(aFoodItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFoodItems()) { index = numberOfFoodItems() - 1; }
      foodItems.remove(aFoodItem);
      foodItems.add(index, aFoodItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFoodItemAt(FoodItem aFoodItem, int index)
  {
    boolean wasAdded = false;
    if(foodItems.contains(aFoodItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFoodItems()) { index = numberOfFoodItems() - 1; }
      foodItems.remove(aFoodItem);
      foodItems.add(index, aFoodItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addFoodItemAt(aFoodItem, index);
    }
    return wasAdded;
  }

  public boolean isNumberOfBillsValid()
  {
    boolean isValid = numberOfBills() >= minimumNumberOfBills();
    return isValid;
  }

  public static int minimumNumberOfBills()
  {
    return 1;
  }

  public boolean addBill(Bill aBill)
  {
    boolean wasAdded = false;
    if (bills.contains(aBill)) { return false; }
    bills.add(aBill);
    if (aBill.indexOfOrder(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aBill.addOrder(this);
      if (!wasAdded)
      {
        bills.remove(aBill);
      }
    }
    return wasAdded;
  }

  public boolean removeBill(Bill aBill)
  {
    boolean wasRemoved = false;
    if (!bills.contains(aBill))
    {
      return wasRemoved;
    }

    if (numberOfBills() <= minimumNumberOfBills())
    {
      return wasRemoved;
    }

    int oldIndex = bills.indexOf(aBill);
    bills.remove(oldIndex);
    if (aBill.indexOfOrder(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aBill.removeOrder(this);
      if (!wasRemoved)
      {
        bills.add(oldIndex,aBill);
      }
    }
    return wasRemoved;
  }

  public boolean setBills(Bill... newBills)
  {
    boolean wasSet = false;
    ArrayList<Bill> verifiedBills = new ArrayList<Bill>();
    for (Bill aBill : newBills)
    {
      if (verifiedBills.contains(aBill))
      {
        continue;
      }
      verifiedBills.add(aBill);
    }

    if (verifiedBills.size() != newBills.length || verifiedBills.size() < minimumNumberOfBills())
    {
      return wasSet;
    }

    ArrayList<Bill> oldBills = new ArrayList<Bill>(bills);
    bills.clear();
    for (Bill aNewBill : verifiedBills)
    {
      bills.add(aNewBill);
      if (oldBills.contains(aNewBill))
      {
        oldBills.remove(aNewBill);
      }
      else
      {
        aNewBill.addOrder(this);
      }
    }

    for (Bill anOldBill : oldBills)
    {
      anOldBill.removeOrder(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean addBillAt(Bill aBill, int index)
  {  
    boolean wasAdded = false;
    if(addBill(aBill))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBills()) { index = numberOfBills() - 1; }
      bills.remove(aBill);
      bills.add(index, aBill);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBillAt(Bill aBill, int index)
  {
    boolean wasAdded = false;
    if(bills.contains(aBill))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBills()) { index = numberOfBills() - 1; }
      bills.remove(aBill);
      bills.add(index, aBill);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBillAt(aBill, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<OrderItem> copyOfOrderItems = new ArrayList<OrderItem>(orderItems);
    orderItems.clear();
    for(OrderItem aOrderItem : copyOfOrderItems)
    {
      if (aOrderItem.numberOfOrders() <= OrderItem.minimumNumberOfOrders())
      {
        aOrderItem.delete();
      }
      else
      {
        aOrderItem.removeOrder(this);
      }
    }
    for(int i=foodItems.size(); i > 0; i--)
    {
      FoodItem aFoodItem = foodItems.get(i - 1);
      aFoodItem.delete();
    }
    while (bills.size() > 0)
    {
      Bill aBill = bills.get(bills.size() - 1);
      aBill.delete();
      bills.remove(aBill);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "date" + ":" + getDate()+ "," +
            "time" + ":" + getTime()+ "]";
  }
}