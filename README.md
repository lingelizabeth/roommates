Roommate Hub App Design Project - README Template
===
Lizzy Ling

# ROOMMATE HUB

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
An app that centralizes all the logistical aspects of living with roommates, including finances, chores, calendars, groceries, and a bucket list. It may integrate with the Google Calendar API to create and send notifications for events.

### App Evaluation
- **Category:** Utility
- **Mobile:** Mobile First (Android), Web version would also be useful
- **Story:** Roommates can quickly refer to necessary aspects of their living situation rather than having multiple documents and apps. The app would remind each person about rent, events, and chores, and it also enables easy file sharing for shopping lists or bucket lists. 
- **Market:** Anyone living with roommates would be able to use this app. It could also be used for a group vacation or extended outing.
- **Habit:** Roommates use this throughout the day to be reminded of tasks or to add information when they think of it.
- **Scope:** V1 would create shared files such as important info, grocery lists, and chores. V2 would integrate a calendar to add events and reminders. V3 would add functionality to record expenses.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Authentication: Users are able to sign in with an account
* Group creation: Users can create or join an existing private roommate group
* File sharing functionality: Users can view and edit pages that all members of a group see, especially a grocery list 
* Users can create a shared chores table 

**Optional Nice-to-have Stories**

* Send notifications to remind users about chores
* Google Calendar integration to create events
* Splitwise integration to track expenses

### 2. Screen Archetypes

* Login or register screens
   * User story: Authentication
   * Possibly connect with Google account to use calendar
* Profile (Personal groups)
    * Shows all your groups
    * User story: Users can create or join an existing roommate group
* Roommate Hub Homepage
    * Show relevant info such as address, wifi, rent
* Chores Page
   * User story: Shared files
   * Stretch task is notifications
* Notes page
    * Includes grocery list and bucket list 
* Calendar page
    * Add house events
    * Send notifications for events
* Finances page
    * Record finances for the house
* Group page 
    * Shows where each person is (home or not home)

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Calendar
* Chores
* Finances
* Notes

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Authentication
   * Leads to join group 
* Profile
   * Recycler View representing each roommate group
       * Clicking each one leads to roommatet home which has the above 5 tabs
* Roommate Home
   * Navigate to using bottom tabs
* Calendar
   * Navigate to using bottom tabs
   * Might link to external Calendar app
* Chores
   * Navigate to using bottom tabs
   * Might have a detail view depending on layout
* Finances
   * Navigate to using bottom tabs
   * Might link to Venmo or Splitwise
* Notes
   * Navigate to using bottom tabs
   * Recycler view of all notes
       * Clicking leads to notes detail view
       * "New Note" button or "Edit" leads to an edit view

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
