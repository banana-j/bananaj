# bananaj
Simple api for accessing Mailchimp - Work in progess

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/gr4h4n/bananaj/master/LICENSE.md)

# Introduction

bananaj provides an Java wrapper for the MailChimp API 3.0. It is possible access your MailChimp data through Java. 

# How to use

## Add to your project 
This is still in alpha. If you encounter some bugs or issues, please feel free to report them to the [Issues section](https://github.com/banana-j/bananaj/issues).

Add this dependency to your pom.xml to use **bananaj** in your project.
```
<dependency>
  <groupId>com.github.banana-j</groupId>
  <artifactId>bananaj</artifactId>
  <version>0.6.2</version>
</dependency>
```
or with Gradle

``` 
repositories {
    maven { url "http://repo.maven.apache.org/maven2" }
}

dependencies {
    compile group: 'com.github.banana-j', name: 'bananaj', version: '0.6.2'
}
```

If you are not using Maven or Gradle you can download the latest `fat jar` from the [releases section](https://github.com/banana-j/bananaj/releases).

## MailChimpObject class
Most of the com.github.alexanderwe.bananaj.model classes extend the MailChimpObject class.They are immutable, to prevent asynchronous data between the client and the MailChimp server. 
When you execute methods like `object.changeName("new Name")` the local and server data is updated simultaneously.

Most of the com.github.alexanderwe.bananaj.model classes can also be exported to JSON.

## Initial com.github.alexanderwe.bananaj.connection
With the MailChimpConnection object you start to connect to your account. 
You can get all objects from this com.github.alexanderwe.bananaj.connection. First start with getting information about a list.

```
MailChimpConnection con = new MailChimpConnection("yourAPIkey");
```
## Get a list 
```
//Get all lists
List<MailChimpList> allLists = con.getLists();
```
```
//Get a single list
MailChimpList yourList = con.getList("ListID");
```

## Create a list

## Get Members
```
/*Get all members from a specific mailChimpList*/
 List<Member> membersOfList = yourList.getMembers(0,0); // Get all members, skip none
 List<Member> partOfMembers = yourList.getMembers(5,2); // Get first 5 members, but skip 2 members 
```
```
/*You can also get a specific member by specifying it's id*/
Member memberOfList = yourList.getMember("MemberID");
```

## Add/Update list subscriber
```
Map<String, String> mergeFields = new HashMap<String, String>();
mergeFields.put("FNAME", "First");
mergeFields.put("LNAME", "Last");
String ipAddress = "127.0.0.1";
LocalDateTime timeStamp = LocalDateTime.now();
Map<String, Boolean> interests  = new HashMap<String, Boolean>();
interests.put("12345", true);

Member member = new Member.Builder()
		.emailAddress("myEmail@my.domain.com")
		.list(yourList)
		.emailType(EmailType.HTML)
		.status(MemberStatus.SUBSCRIBED)
		.mergeFields(mergeFields)
		.statusIfNew(MemberStatus.SUBSCRIBED)
		.ipSignup(ipAddress)
		.timestampSignup(timeStamp)
		.ipOpt(ipAddress)
		.timestampOpt(timeStamp)
		.memberInterest(interests)
		.build();
yourList.addOrUpdateMember(member);
```


## Create template
To create an email template simply specify a template name and the upload the pure html code to MailChimp
```
con.createTemplate("templateName", "htmlCode");
```


## Create campaign
To create an email template simply specify a template name and the upload the pure html code to MailChimp
```
MailChimpList myList = con.getList("myListId");
CampaignSettings settings = new CampaignSettings.Builder()
		.title("myTitle")
		.subjectLine("mySubject")
		.toName("*|FNAME|*")
		.fromName("myRobot")
		.replyTo("myEmail@my.domain.com")
		.templateId(12345)
		.folderId("12345")
		.build();
Campaign campaign = con.createCampaign(CampaignType.REGULAR, myList, settings);
```


## Upload a file to FileManager
MailChimp offers the opportunity to insert images and other files to your emails. To upload a file to MailChimp create a FileManager and specify the file you want to upload.
```
FileManager fileManager = new FileManager(mailchimpconnection);

File myFile = new File("pathToYourFile");
  
//Upload a file
fileManager.upload("filename", myFile);
  
//Upload a file to a folder
fileManager.upload("folder_id","filename", myFile);
```

## Download a file
To download a file from the MailChimp File Manager you have to specifiy the file you want to download and the directory in which it should be saved after download. The file extension is set automatically.
```
fileManager.getFileManagerFile("fileID").download("./") //Download a file in the current directory
```

## Methods
Every endpoint supports GET, POST, and DELETE requests. So it is possible to fully control your MailChimp objects with this wrapper. 

### Endpoints used

- **"https://"+server+".api.mailchimp.com/3.0/"**
- **"https://"+server+".api.mailchimp.com/3.0/lists"**
- **"https://"+server+".api.mailchimp.com/3.0/campaign-folders"**
- **"https://"+server+".api.mailchimp.com/3.0/campaigns"**
- **"https://"+server+".api.mailchimp.com/3.0/template-folders"**
- **"https://"+server+".api.mailchimp.com/3.0/templates"**
- **"https://"+server+".api.mailchimp.com/3.0/automations"**
- **"https://"+server+".api.mailchimp.com/3.0/file-manager/folders"**
- **"https://"+server+".api.mailchimp.com/3.0/file-manager/files"**
- **"https://"+server+".api.mailchimp.com/3.0/file-manager/reports"**

# To do 
- Add missing edit function to the different endpoints 

# Contributors
 
Thanks a lot to all contributors:

* [gscriver](https://github.com/gscriver)
* [bphilipnyc](https://github.com/bphilipnyc)
* [icu222much](https://github.com/icu222much)

# License
The MIT License (MIT)

Copyright (c) 2015 - 2018 Alexander Weiß

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
