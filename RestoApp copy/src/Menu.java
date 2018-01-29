/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 19 "iteration1.ump"
public class Menu
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Menu Attributes
  private String courses;

  //Menu Associations
  private Restaurant restaurant;
  private List<FoodItem> foodItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Menu(String aCourses, Restaurant aRestaurant)
  {
    courses = aCourses;
    if (aRestaurant == null || aRestaurant.getMenu() != null)
    {
      throw new RuntimeException("Unable to create Menu due to aRestaurant");
    }
    restaurant = aRestaurant;
    foodItems = new ArrayList<FoodItem>();
  }

  public Menu(String aCourses, int aNumTablesForRestaurant, int aNumSeatsForRestaurant, int aFloorLayoutForRestaurant)
  {
    courses = aCourses;
    restaurant = new Restaurant(aNumTablesForRestaurant, aNumSeatsForRestaurant, aFloorLayoutForRestaurant, this);
    foodItems = new ArrayList<FoodItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCourses(String aCourses)
  {
    boolean wasSet = false;
    courses = aCourses;
    wasSet = true;
    return wasSet;
  }

  public String getCourses()
  {
    return courses;
  }

  public Restaurant getRestaurant()
  {
    return restaurant;
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

  public boolean isNumberOfFoodItemsValid()
  {
    boolean isValid = numberOfFoodItems() >= minimumNumberOfFoodItems();
    return isValid;
  }

  public static int minimumNumberOfFoodItems()
  {
    return 1;
  }

  public FoodItem addFoodItem(String aItem, int aPrice, Order aOrder)
  {
    FoodItem aNewFoodItem = new FoodItem(aItem, aPrice, aOrder, this);
    return aNewFoodItem;
  }

  public boolean addFoodItem(FoodItem aFoodItem)
  {
    boolean wasAdded = false;
    if (foodItems.contains(aFoodItem)) { return false; }
    Menu existingMenu = aFoodItem.getMenu();
    boolean isNewMenu = existingMenu != null && !this.equals(existingMenu);

    if (isNewMenu && existingMenu.numberOfFoodItems() <= minimumNumberOfFoodItems())
    {
      return wasAdded;
    }
    if (isNewMenu)
    {
      aFoodItem.setMenu(this);
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
    //Unable to remove aFoodItem, as it must always have a menu
    if (this.equals(aFoodItem.getMenu()))
    {
      return wasRemoved;
    }

    //menu already at minimum (1)
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

  public void delete()
  {
    Restaurant existingRestaurant = restaurant;
    restaurant = null;
    if (existingRestaurant != null)
    {
      existingRestaurant.delete();
    }
    while (foodItems.size() > 0)
    {
      FoodItem aFoodItem = foodItems.get(foodItems.size() - 1);
      aFoodItem.delete();
      foodItems.remove(aFoodItem);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "courses" + ":" + getCourses()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restaurant = "+(getRestaurant()!=null?Integer.toHexString(System.identityHashCode(getRestaurant())):"null");
  }
}