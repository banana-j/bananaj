# bananaj
Simple api for accessing Mailchimp - Work in progess

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/gr4h4n/bananaj/master/LICENSE.md)

# Introduction

bananaj provides a Java wrapper for the MailChimp API 3.0. It is possible programmatically access most MailChimp features such as managing audiences, generate campaigns, and get report data.

# How to use

## Add to your project 
This is still in alpha. Some of the methods and models are subject to change in future builds. If you encounter some bugs or issues, please feel free to report them to the [Issues section](https://github.com/banana-j/bananaj/issues).

Add this dependency to your pom.xml to use **bananaj** in your project.
```
<dependency>
  <groupId>com.github.banana-j</groupId>
  <artifactId>bananaj</artifactId>
  <version>0.7.0</version>
</dependency>
```
or with Gradle

``` 
repositories {
    maven { url "http://repo.maven.apache.org/maven2" }
}

dependencies {
    compile group: 'com.github.banana-j', name: 'bananaj', version: '0.7.0'
}
```

If you are not using Maven or Gradle you can download the latest `fat jar` from the [releases section](https://github.com/banana-j/bananaj/releases).

## Obtain an API key
Login to your Mailchimp account and access you account information. Navigate to API keys located under extras and create a key.

## Connect to Mailchimp
First establish a connection with the Mailchimp server.

```
MailChimpConnection con = new MailChimpConnection("yourAPIkey");

// Test connection
boolean ping = con.ping();
System.out.println("API Ping: " + (ping ? "succeed" : "failed"));
```

## Access your campaigns

```
// Print info for all campaigns with report and feedback
    System.out.println("Mailchimp Campaigns:");
    con.getCampaigns().forEach(campaign -> {
        System.out.println(campaign.toString());

        Report report = campaign.getReport();
        System.out.println(report.toString());

        List<CampaignFeedback> feedback = campaign.getFeedback().forEach(feedback -> {
            System.out.println(feedback.toString());
        });;
        
        System.out.println("");
    });
    System.out.println("");
```

## Access your audiences
```
// Print info for all audiences. Note that in the API an audience is called a list.
    System.out.println("Mailchimp audiences:");
    con.getLists().forEach(audiance -> {
        System.out.println(audiance.toString());
        System.out.println("");
    });
    System.out.println("");

// Get a single audience
    MailChimpList yourList = con.getList("ListID");

// Add a member to an audience

```

## Get Members
```
// Get all members from a specific mailChimpList
    yourList.getMembers().forEach(member -> {
        System.out.println(member.toString());
    });

// You can also get a specific member by specifying their email address or subscriber hash
    Member memberOfList = yourList.getMember("name@domain.com");
```

## Add/Update list subscriber
```
    Member member = new Member.Builder()
            .connection(con)
            .listId(yourList.getId())
            .emailAddress("name@domain.com")
            .emailType(EmailType.HTML)
            .status(MemberStatus.SUBSCRIBED)
            .mergeField("FNAME", "John")
            .mergeField("LNAME", "Smith")
            .language("en")
            .vip(false)
            .build();
    member.addOrUpdate();
```


## Create campaign
To create an email template simply specify a template name and the upload the pure html code to MailChimp
```
    CampaignRecipients mailRecipients = new CampaignRecipients.Builder()
        .listId(yourList.getId())
        .build();
    // -or- use new CampaignRecipients(Segment) to target by tag or interest group
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
    // campaign.getContent().changeHTMLContent(htmlContent);  // if we don't use a template specify our own HTML content
    campaign.send();
    
    // TODO: show how to create campaign targeted to a tag segment.
```


## Upload a file to FileManager
MailChimp offers the opportunity to insert images and other files to your emails. To upload a file to MailChimp create a FileManager and specify the file you want to upload.
```
// Upload a file
    FileManagerFile myFile = con.getFileManager().upload("filename", myFile);
  
// Upload a file to a folder
    FileManagerFile myFile = con.getFileManager().upload("folder_id", "filename", myFile);
```

## Example programs
* [chimpexcel](https://github.com/gscriver/chimpexcel) A sample Java program to export Mailchimp audiences to file (XLS).


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
- **"https://"+server+".api.mailchimp.com/3.0/reports"**

# To do 
- Add missing edit function to the different endpoints 

# Contributors
 
Thanks a lot to all contributors:

* [alexanderwe](https://github.com/alexanderwe)
* [gscriver](https://github.com/gscriver)
* [bphilipnyc](https://github.com/bphilipnyc)
* [icu222much](https://github.com/icu222much)

# License
The MIT License (MIT)

Copyright (c) 2015 - 2020 Alexander Weiß

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
