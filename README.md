#bananaj
Simple api for accessing Mailchimp - Work in progess

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/gr4h4n/bananaj/master/LICENSE.md)
[![GitHub version](https://img.shields.io/badge/version-v1.1.0--alpha-orange.svg)](https://github.com/gr4h4n/bananaj/releases/tag/v1.1.0-alpha)
[![GitHub version](https://img.shields.io/badge/coverage-45%25-FFEB3B.svg)](https://github.com/gr4h4n/bananaj)



#Introduction

bananaj provides an Java wrapper for the Mailchimp API 3.0. It is possible acces your Mailchimp data through Java. All Mailchimp objects are mapped to Java objects which can be used to write your own java program. Each Object can also be exported to json.

#How to use

##Add to your project 
At this point of development this artifact is not hosted at Macen Central. This will come in a future release. To implement this artifact in your project 
simply donwload the .jar file from [this repository](https://github.com/gr4h4n/bananaj/blob/master/bananaj-1.1.0-alpha.jar) and add it as an external library to your project. 


## Initial connection
With the MailchimpConnection object you start to connect to your account. All starts with a connection object. 
You can get all objects from this connection. First start with getting mailChimpList informations.

```
MailchimpConnection con = new MailchimpConnection("yourAPIkey");
con.getLists(); // get all mailChimpLists in your account
```

##Get mailChimpLists
```
//Get all lists
ArrayList<MailChimpList> allLists = con.getLists();
```
```
//Get a single list
MailChimpList yourList = con.getList("ListID");
```

##Get Members
```
/*Get all members from a specific mailChimpList*/
 ArrayList<Member> membersOfList = yourList.getMembers(0); // Get all members 
 ArrayList<Member> partOfMembers = yourList.getMembers(5); // Get first 5 members 
```
```
/*You can also get a specific member by specifying it's id*/
Member memberOfList = yourList.getMember("MemberID");
```


##Create template
To create an email template simply specify a template name and the upload the pure html code to MailChimp
```
con.addTemplate("templateName", "htmlCode");
```


##Upload a file to FileManager
```
FileManager fileManager = new FileManager(mailchimpconnection);

File myFile = new File("pathToYourFile");
  
//Upload a file
fileManager.upload("filename", myFile);
  
Upload a file to a folder
fileManager.upload("folder_id","filename", myFile);
```

##Methods
Every endoint supports GET, POST, and DELETE requests. So it is possible to fully control your MailChimp objects with this wrapper. 

###Endpoints used

- **"https://"+server+".api.mailchimp.com/3.0/"**
- **"https://"+server+".api.mailchimp.com/3.0/lists"**
- **"https://"+server+".api.mailchimp.com/3.0/campaign-folders"**
- **"https://"+server+".api.mailchimp.com/3.0/campaigns"**
- **"https://"+server+".api.mailchimp.com/3.0/template-folders"**
- **"https://"+server+".api.mailchimp.com/3.0/templates"**
- **"https://"+server+".api.mailchimp.com/3.0/automations"**
- **"https://"+server+".api.mailchimp.com/3.0/file-manager/folders"**
- **"https://"+server+".api.mailchimp.com/3.0/file-manager/files"**


#Package structure
```
+---connection
|       Account.java
|       Connection.java
|       MailchimpConnection.java
|       
+---exceptions
|       EmailException.java
|       FileFormatException.java
|       
\---model
    |   MailchimpObject.java
    |   
    +---automation
    |       Automation.java
    |       AutomationStatus.java
    |       
    +---campaign
    |       Bounce.java
    |       Campaign.java
    |       CampaignContent.java
    |       CampaignDefaults.java
    |       CampaignFolder.java
    |       CampaignSettings.java
    |       CampaignStatus.java
    |       CampaignType.java
    |       
    +---conversation
    |       Conversation.java
    |       
    +---filemanager
    |       FileManager.java
    |       FileManagerFile.java
    |       FileManagerFolder.java
    |       
    +---list
    |   |   GrowthHistory.java
    |   |   MailChimpList.java
    |   |   
    |   +---member
    |   |       Member.java
    |   |       MemberActivity.java
    |   |       MemberStatus.java
    |   |       
    |   +---mergefield
    |   |       MergeField.java
    |   |       MergeFieldOptions.java
    |   |       
    |   \---segment
    |           Options.java
    |           Segment.java
    |           SegmentType.java
    |           
    +---report
    |       Click.java
    |       FacebookLikes.java
    |       Forward.java
    |       IndustryStats.java
    |       Open.java
    |       Report.java
    |       ReportListStats.java
    |       
    \---template
            Template.java
            TemplateFolder.java
            TemplateType.java
```

# To do 
- Add artifact to Maven Central
- Add missing edit function to the different endpoints 
- Clean up code


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
