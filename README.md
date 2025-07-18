# Election-Analysis
This Java project provides a data visualization tool for analyzing historical U.S. House and Senate election results. It parses a CSV file of past elections and organizes the data using custom linked data structures. A graphical interface allows users to explore voter trends, candidate details, and winning parties across states and years.


**Class Overview**
ElectionAnalysis:
* Core logic for parsing and storing election data
* Organizes data using:

* YearNode (singly linked list of election years)

* StateNode (circular linked list of states within each year)

* ElectionNode (singly linked list of elections in each state)

**Driver**
Main GUI controller:
* Renders the interactive map and handles user inputs for navigation

ElectionNode: 
* Represents a single election race (House or Senate)
* Stores parallel lists of candidates, parties, votes, and winner

StateNode:
* Represents a state within a particular election year
* Links to its elections and to the next state in the circular list

YearNode:
* Represents a specific year

Links to a circular list of states and the next year
