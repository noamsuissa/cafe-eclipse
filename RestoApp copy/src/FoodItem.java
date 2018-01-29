/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/



// line 27 "iteration1.ump"
public class FoodItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //FoodItem Attributes
  private String item;
  private int price;

  //FoodItem Associations
  private Order order;
  private Menu menu;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public FoodItem(String aItem, int aPrice, Order aOrder, Menu aMenu)
  {
    item = aItem;
    price = aPrice;
    boolean didAddOrder = setOrder(aOrder);
    if (!didAddOrder)
    {
      throw new RuntimeException("Unable to create foodItem due to order");
    }
    boolean didAddMenu = setMenu(aMenu);
    if (!didAddMenu)
    {
      throw new RuntimeException("Unable to create foodItem due to menu");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setItem(String aItem)
  {
    boolean wasSet = false;
    item = aItem;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public String getItem()
  {
    return item;
  }

  public int getPrice()
  {
    return price;
  }

  public Order getOrder()
  {
    return order;
  }

  public Menu getMenu()
  {
    return menu;
  }

  public boolean setOrder(Order aOrder)
  {
    boolean wasSet = false;
    //Must provide order to foodItem
    if (aOrder == null)
    {
      return wasSet;
    }

    if (order != null && order.numberOfFoodItems() <= Order.minimumNumberOfFoodItems())
    {
      return wasSet;
    }

    Order existingOrder = order;
    order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder))
    {
      boolean didRemove = existingOrder.removeFoodItem(this);
      if (!didRemove)
      {
        order = existingOrder;
        return wasSet;
      }
    }
    order.addFoodItem(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setMenu(Menu aMenu)
  {
    boolean wasSet = false;
    //Must provide menu to foodItem
    if (aMenu == null)
    {
      return wasSet;
    }

    if (menu != null && menu.numberOfFoodItems() <= Menu.minimumNumberOfFoodItems())
    {
      return wasSet;
    }

    Menu existingMenu = menu;
    menu = aMenu;
    if (existingMenu != null && !existingMenu.equals(aMenu))
    {
      boolean didRemove = existingMenu.removeFoodItem(this);
      if (!didRemove)
      {
        menu = existingMenu;
        return wasSet;
      }
    }
    menu.addFoodItem(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Order placeholderOrder = order;
    this.order = null;
    if(placeholderOrder != null)
    {
      placeholderOrder.removeFoodItem(this);
    }
    Menu placeholderMenu = menu;
    this.menu = null;
    if(placeholderMenu != null)
    {
      placeholderMenu.removeFoodItem(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "item" + ":" + getItem()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "menu = "+(getMenu()!=null?Integer.toHexString(System.identityHashCode(getMenu())):"null");
  }
}