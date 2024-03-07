# EPatwari | A Blockchain and MYSQL Based Storage for Land Ownership Details

**EPatwari** is Blockchain & MYSQL based storage software for Land Registry Papers. It solves common and critical problems related to land ownership details. Specifically, **false land ownership claims, double selling, double mortgage and loss of official documents.**

- Blockchain based features include *decentralized storage, immutable history of land ownership transfer and easy data alteration detection*.
- MYSQL is used for easy *data mining* from blockchain.

**Note:** Blockchain (stored data) doesn't depend on MYSQL in any way. MYSQL is only for data mining purposes. EPatwari offers program, blockchain and database indepedence. Data is stored as a serialized object which is linkedlist of all nodes in blockchain.

## Development Log
Here's an interesting log of the project's development progress (excluding Data Modeling/UML creation and SQL queries), including the date of project initiation, hours worked, and cups of tea or coffee consumed:
- Date of starting project: 12/12/2018
- Date, Hours, Cups of tea/coffee: 12/12/2018, 1, 0
- Date, Hours, Cups Of TEA, Line Count:  13/12/2018, 6, 2, 769 - 115 blank lines
- Date, Hours, Cups Of TEA, Line Count:  15/12/2018, 9, 3, 1791 - 274 blank lines
- Date, Hours, Cups Of TEA, Line Count:  16/12/2018, 7, 2, 2307 - 274 blank lines
- Date, Hours, Cups Of TEA, Line Count:  18/12/2018, 4, 1, 2495 - 274 blank lines

## Features
- *Object-Orientated Programming concepts* are followed for easy Reuse and Extension of software.
- *Database design* is done properly after ERD to schema mapping algorithm and normalization process (after finding out functional dependencies).
- *Mini-world entities* (i-e: Land, Property, Person, Organization)are properly created and mapped in database and software
- *Software, blockchain, and database independence*.
- *MYSQL is not involved for data storage* and retrieval. It is only used for data
exporting purposes where database is needed to analyze data. 
- *Blockchain to MYSQL exporter* for Data Scientists
- *Serialized blockchain object* (stored data) which can be transferred to other machines
easily (for decentralization)


## Getting Started

1. Import MYSQL Connector library
2. To import current data of official land documents, store their details in csv files. Format for that file is: Land Details, Owners, Heirs. Each owner and heir is separated by colon (:) and each attribute of same enitity is separated by semicolon (;). Example can be found in sample files.
     - personOwnerships file is for lands which have humans as their owners. 
     - organizationOwnerships file is for lands which have organizations as their owners.
3. Uncomment line in Main class which executes getStarted() and run program. Ignore file not found exception for epatwariData.bat file.
4. Remove getStarted() line.
5. Now program can be converted into Jar file and setup is completed.

### Configuring Database Settings
1. Create database using these details

**Name:** Epatwari

**User:** Root

**Password:** hasnain

 **-OR-**

You can change these details in method connectAndImportMysql() method in Main class by changing following code:
```String url = "jdbc:mysql://127.0.0.1:3306/epatwari";
Properties info = new Properties();
info.put("user", "root");
info.put("password", "hasnain"); 
```

2. Run queries to create tables in database

**Optional:**

3. Run software and export data from blockchain to MYSQL.
4. Run queries to mine useful information (Example queries are given)

## Features to be implemented
- Database part is not fully functional (owns table is not filled when data is exported from blockchain to MYSQL. But other tables are working fine.
- Peer to peer protocol is to be programmed to make it decentralized.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
