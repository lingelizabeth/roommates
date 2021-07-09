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

* [ ] Authentication: Users are able to sign in with an account
* [ ] Group creation: Users can create or join an existing private roommate group
* [ ] File sharing functionality: Users can view and edit pages that all members of a group see, especially a grocery list 
* [ ] Users can create a shared chores table 

**Optional Nice-to-have Stories**

* [ ] Send notifications to remind users about chores
* [ ] Google Calendar integration to create events
* [ ] Splitwise integration to track expenses
* [ ] Customizable home page that can add widgets (Fragments) for chores or calendar

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
<img src="https://user-images.githubusercontent.com/31828974/125111420-e1faa600-e0b3-11eb-9f77-47afcaa3af59.png" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
**Model: User**
| Property   | Required? | Type   | Description                                     |
|------------|-----------|--------|-------------------------------------------------|
| username   | *         | String | user created string id                          |
| email      | *         | String | email address                                   |
| password   | *         | String | user password                                   |
| first name | *         | String | user first name                                 |
| last name  | *         | String | user last name                                  |
| photo      | *         | File   | profile photo                                   |
| groups     | *         | Array  | array of pointers to Group objects              |
| friends    |           | Array  | stretch goal: array of pointers to User objects |

**Model: Group**
| Property    | Required? | Type     | Description                                                                                    |
|-------------|-----------|----------|------------------------------------------------------------------------------------------------|
| title       | *         | String   | Group name                                                                                     |
| description |           | String   | optional description of group                                                                  |
| members     | *         | Array    | array of pointers to User objects in this group                                                |
| createdAt   | *         | datetime | date created                                                                                   |
| updatedAt   | *         | datetime | last time this was updated, stretch goal: take into account internal activity like notes edits |
| photo       |           | File     | group photo for banner                                                                         |
| address     |           | String   | address of apartment or house                                                                  |

**Model: Chore**
| Property  | Required? | Type             | Description                                                     |
|-----------|-----------|------------------|-----------------------------------------------------------------|
| group     | *         | pointer to Group | group that this chore belongs to                                |
| dayOfWeek | *         | String           | what day of the week this chore should be done                  |
| Title     | *         | String           | name of chore (ie. dishes, taking out trash, etc.)              |
| user      | *         | pointer to User  | person assigned to this chore                                   |
| checked   |           | boolean          | whether or not this chore has been done, should reset every day |
| isDaily   |           | boolean          | stretch goal: allow for daily and weekly chores                 |

**Model: List**
| Property  | Required? | Type     | Description                            |
|-----------|-----------|----------|----------------------------------------|
| Title     | *         | String   | title of list (ie. grocery list, etc.) |
| createdAt | *         | datetime | date created                           |
| updatedAt | *         | datetime | last time the list was updated         |
| items     | *         | Array    | array of pointers to listitems         |

**Model: ListItem**
| Property         | Required? | Type            | Description                                                    |
|------------------|-----------|-----------------|----------------------------------------------------------------|
| text             | *         | String          | list item text                                                 |
| isCheckbox       |           | boolean         | stretch goal: support checkboxes and bullet points             |
| isChecked        |           | boolean         | if is checkbox, this indicates whether the box is checked      |
| createdAt        | *         | datetime        | date created                                                   |
| updatedAt        | *         | datetime        | last time the list was updated                                 |
| currentlyEditing |           | pointer to User | pointer to a user who is currently editing this item, or null  |

### Networking
- [Add list of network requests by screen ]
Profile (Personal Groups) Screen
- (Read/GET) Query all groups where user is a member OR Query list of groups from user 

Create group modal
- (Create/POST) Create a new group

Roommate Hub Homepage
- (Read/GET) Query all important info from group (ie. welcome message, wifi, etc.)
- (Read/GET) Query a list of today’s chores
- (Read/GET) Stretch goal: query all notes others have left on the “fridge” 

Chores page
- (Read/GET) Query all chores for this day of the week
- (Update/PUT) Mark a chore as complete
- (Create/POST) Create a new chore
- (Delete) Delete a chore

Notes page
- (Read/GET) Query all Lists
- (Create/POST) Create a new list
- (Delete) Delete a list

Notes Detail Page
- (Read/GET) Query all ListItems within a list
- (Create/POST) Create a new listitem
- (Update/PUT) Mark an item as checked
- (Update/PUT) Edit an item text
- (Delete) Delete an item


- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
