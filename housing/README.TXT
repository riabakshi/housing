PROJECT TITLE: Ria's Housing Assigment
PURPOSE OF PROJECT:
Create a project that represents the housing assigments at IMSA.

VERSION or DATE: 12/8/24
HOW TO START THIS PROJECT: 
Run the main method on the Analyzer. 
This will automatically read the preference information 
and sort students into wings. 
It will then print all the rooming assignments and some statistics.

AUTHORS: Ria Bakshi
USER INSTRUCTIONS:

1. Call the main method on the Analyzer. This will automatically read
the preference information from the preferences.txt file and sort students into dorms.
It will print housing-assignments to the housing-assignments.txt file. 
It will print some statistical information into the statistics.txt file
which holds info about how many people got their first choice hall, and the 
wing, gender, and number of people in each wing to the statistics.txt file.  

The methodology for the following methods can be found in the code comments, however a brief overview
    is provided below. 
- The sort method for halls can be found in the Analyzer class. 
    This method randomly orders all student preference data, and starts doling out preferred halls. 
    If students are not sorted into their top three halls, they are re-sorted to try and make
    these accomadations. 
    
- The sort method for wings can be found in the Hall class. 
    This method randomly orders student preference data for each hall. It then sorts students into priority
    and regular rankings, based on whether this hall is their first choice hall. 
    If they are priority, then their wing preferences are considered. If they are regular, they are not.
    
- The sort method for rooms can be found in the Wings class. 
    This method currently lacks any extra implementation. It simply orders students to available rooms.

Future implemetations 
* add other housing data including information about preffered wingmates and desired room types

Encapsulation: 
The protected field is a type of field which  protects information between
super and child classes. This is a type of encapsulation because it allows for both the sharing
and hiding of methods. 
All methods regarding data is stored within its own class. 
For example, the sortIntoWings method is unique to the Wing instead of the analyzer
because it regards organizing the roommate pairs stored in the wing. 

Abstraction: 
Each class represents different information required to solve this problem. 
These can be used wihtout needing to understand all the logic within them and makes it easier 
to continue to maintain and extend the implementation of this project. 
For example, the sortIntoHall or sortIntoWing methods have similar names and perform
similar functions, but they are implemented in different ways. 

Inheritence: 
* both the wing and the hall classes inherit from the grouping class, which represents a grouping of students.
* this allows for the sharing of methods regarding keeping track of roommate pairs, getting statistical info from the roommate pairs, and the gender of each grouping. 

Polymorphism: 
The checkEligibility method is present in both the wing and hall classes
However, it checks different conditions within each of them. 


