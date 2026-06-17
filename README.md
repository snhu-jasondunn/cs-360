# cs-360
CS-360-12080-M01 Mobile Architect &amp; Programming 2026

# Module Eight Journal Guidelines

## Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?

The mobile application I developed was an Inventory Management App designed to help warehouse employees efficiently track and manage inventory items. The app requirements included a secure login system, a database to store user credentials and inventory records, a grid view displaying inventory items, the ability to add and remove products, controls to increase or decrease item quantities, and a notification system to alert users when inventory levels reached zero. The primary user need addressed by this application was providing an organized and reliable method for monitoring stock levels and maintaining accurate inventory records. By centralizing inventory information in a mobile application, users can quickly update and access inventory data without relying on manual tracking methods.

## What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?

Several screens and features were necessary to create a user-centered experience. The login screen provided secure access and allowed new users to create accounts. The inventory screen displayed all inventory items in an organized grid format, making it easy to view current stock levels. Additional features included buttons for adding new items, deleting items, and adjusting inventory quantities. The application also included SMS notification functionality to alert users when an item's quantity reached zero.

My UI design focused on simplicity and ease of use. Navigation was straightforward, buttons were clearly labeled, and important information was displayed prominently. Users could complete common tasks with minimal steps, reducing confusion and improving efficiency. These design choices were successful because they allowed users to quickly understand how to interact with the application without requiring extensive training or technical knowledge.

## How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?

I approached the coding process by breaking the application into smaller, manageable components. I first created the database structure and implemented user authentication before developing inventory management features. After the core functionality was working, I added user interface elements and notification capabilities.

One strategy I used was incremental development, where I completed and tested one feature at a time before moving on to the next. I also relied on modular coding practices by separating database operations, user interface components, and business logic into distinct sections. These techniques made troubleshooting easier and reduced the likelihood of introducing errors into previously completed features. In future development projects, I can apply these same strategies to improve organization, maintainability, and overall code quality.

## How did you test to ensure your code was functional? Why is this process important, and what did it reveal?

Testing was performed using the Android Emulator throughout the development process. I tested user registration and login functionality, verified that inventory records could be added, updated, and deleted correctly, and confirmed that notifications were triggered when inventory quantities reached zero. I also tested the application's user interface to ensure buttons, forms, and navigation worked as intended.

Testing is important because it identifies issues before the application reaches users. Through testing, I discovered and corrected several problems related to database interactions and user input validation. The testing process also helped verify that all application requirements were met and that the app performed consistently under different scenarios.

## Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?

One of the most challenging aspects of development was implementing the inventory notification feature. Since the application needed to alert users when inventory levels reached zero, I had to create logic that continuously checked inventory quantities and triggered the appropriate notification behavior. Additionally, I needed to ensure the application would continue functioning even if SMS permissions were not granted. To overcome this challenge, I designed the notification feature to be optional while maintaining all core inventory management functionality. This approach improved the application's reliability and user experience.

## In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?

I was particularly successful in developing the inventory management system and database integration. This component required creating and managing SQLite database tables, performing CRUD (Create, Read, Update, Delete) operations, and displaying inventory information dynamically within the user interface. Successfully implementing these features demonstrated my understanding of database management, Android application architecture, and user-centered design principles. The completed inventory management functionality served as the foundation of the application and effectively met the project's primary objectives.
