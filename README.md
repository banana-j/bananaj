#bananaj
Simple api for accessing Mailchimp - Work in progess

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/gr4h4n/mailchimpwrapper/master/license.md)
[![GitHub version](https://img.shields.io/badge/version-v1.0.0--alpha-orange.svg)](https://github.com/gr4h4n/mailchimpwrapper/releases/tag/v1.0.0-alpha)
[![GitHub version](https://img.shields.io/badge/coverage-40%25-FFEB3B.svg)](https://github.com/gr4h4n/mailchimpwrapper)



#Introduction

bananaj provides an Java wrapper for the Mailchimp API 3.0. With a lot of functions implemented it will be easy to acces your Mailchimp data through Java. All Mailchimp objects are mapped to Java objects which can be used to write your own java program. Each Object can also be exported to json.

#How to use
## Initial connection
With the MailchimpConnection object you start to connect to your account. All starts with a connection object. 
You can get all objects from this connection. First start with getting list informations.

    MailchimpConnection con = new MailchimpConnection("yourAPIkey");
    con.getLists(); // get all lists in your account
    
##Get Members
        MailchimpList listname = con.getList("listId");
        /*Get all members from a specific list*/
        ArrayList<Member> membersOfList = listname.getMembers();
      
        /*You can also get a specific member by specifying it's id*/
        Member memberOfList = listname.getMember("MemberID");
##Upload file to FileManager
        FileManager fileManager = new FileManager(mailchimpconnection);
        fileManager.upload("Filename", yourFileToUpload);

#Package structure
   
    .
    ├── README.md
    ├── bananaj.iml
    ├── lib
    │   ├── commons-validator-1.5.0.jar
    │   ├── jxl.jar
    │   └── org.json-20120521.jar
    ├── out
    │   └── artifacts
    │       └── bananaj_jar
    │           └── bananaj.jar
    ├── pom.xml
    └── src
        └── main
            ├── java
            │   ├── connection
            │   │   ├── Account.java
            │   │   └── MailchimpConnection.java
            │   ├── exceptions
            │   │   └── emailException.java
            │   └── model
            │       ├── MailchimpObject.java
            │       ├── automation
            │       │   ├── Automation.java
            │       │   └── AutomationStatus.java
            │       ├── campaign
            │       │   ├── Bounce.java
            │       │   ├── Campaign.java
            │       │   ├── CampaignContent.java
            │       │   ├── CampaignDefaults.java
            │       │   ├── CampaignSettings.java
            │       │   ├── CampaignStatus.java
            │       │   └── CampaignType.java
            │       ├── conversation
            │       │   └── Conversation.java
            │       ├── filemanager
            │       │   ├── FileManager.java
            │       │   ├── FileManagerFile.java
            │       │   └── FileManagerFolder.java
            │       ├── list
            │       │   ├── GrowthHistory.java
            │       │   ├── List.java
            │       │   └── member
            │       │       ├── Member.java
            │       │       ├── MemberActivity.java
            │       │       └── MemberStatus.java
            │       ├── report
            │       │   ├── Click.java
            │       │   ├── FacebookLikes.java
            │       │   ├── Forward.java
            │       │   ├── IndustryStats.java
            │       │   ├── Open.java
            │       │   ├── Report.java
            │       │   └── ReportListStats.java
            │       └── template
            │           ├── Template.java
            │           └── TemplateType.java
            └── resources

#License
The MIT License (MIT)

Copyright (c) 2015 - 2016 Alexander Weiß

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
