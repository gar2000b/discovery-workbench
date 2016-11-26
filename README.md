# Discovery Workbench

**PROJECT START DATE**: 1st November 2016

**LICENSE**: https://github.com/gar2000b/discovery-workbench/blob/master/LICENSE.md

![alt text](https://raw.githubusercontent.com/gar2000b/discovery-workbench/master/images/social-insurance-workflow-service.png "Social Insurance Workflow")

^ please click to enlarge and get a clear view of the example topology.

## What is the Discovery Workbench?

The Discovery Workbench is part of a wider Project called Discovery.

Specifically, it is an application orchestrator for coordinating all the applications/services installed on developers local machines.

Let's say you have a workflow/project where you have a collection of microservices + supporting infrastructure that you would like to spin up locally for testing purposes say. This very quickly becomes a tedious task to manage and orchestrate manually.

The initial aim of this tool is to have a workspace with your project topology laid out and configured (as shown in pic above) that can be saved and shared among all members of your team. It keeps everything in one place where you simply have to click a button to spin up and run all your services and any supporting scripts you may have.

## To Run

1. Checkout/clone from above  
2. cd into discovery-workbench  
3. mvn clean package  
4. java -jar desktop/target/discovery-workbench-desktop-1.0-SNAPSHOT-jar-with-dependencies.jar  

## Opening shared workspaces

The **workspaces** are simply saved as files and you can find some examples under workspaces.

Loading an existing workspace is as simple as clicking **'Load'**, enter the path to your workspace e.g: C:\work\my-workspaces\social-insurance-workflow-service and click open.

![alt text](https://raw.githubusercontent.com/gar2000b/discovery-workbench/master/images/open-dialog.png "Open Social Insurance Workflow Service")

## Provisioning Instructions

Currently, the provisioning functionality is under development, so the following instructions are for manually provisioning your infrastructure:

1. Once you have opened up a workspace, the first thing to do is to click on 'Instructions' to see how to install and configure any pre-requisites required for your project. It should contain information like installing Elastsearch and setting environment variables.
2. Once you have gone through the instructions, shutdown, restart the workbench and open the project back up - this is so that it picks up on any env vars you may have set. **Note**: env vars will be getting overridden with internal config vars soon, then you will not have to do this in future.
3. You should now be good to run and spin up all your services.

![alt text](https://raw.githubusercontent.com/gar2000b/discovery-workbench/master/images/instructions-dialog.png "Instructions")

## Running your services

The project should already have been pre-configured (to one extent or the other) by another member of your team and will be ready to be executed after going through the instructions above (this should be a one time time).

The configuration will include a) the topology layout b) wiring up each service with commands c) configuring the order and processing type of each service (will explain further down).

Bringing all the services up together is as simple as clicking **'Start All'** - as each service gets to it's running state, you will see a running flag appear on each service.

**'Stop All'** should be self explanitory :)

You also have the ability to individually start and stop each service/script by clicking it's **play/stop** button.

## Creating a new workspace

### Layout Workspace

Creating a new workspace is as simple as dragging each of the required template types from the left-hand column over to the right-hand workbench.

**Application/Service** - represents any application, service or microservice you wish to include in your topology.

**Infrastructure** - represents any infrastructure service such as a DBMS like MongoDB or Queue like RabbitMQ. These templates are functionally equivalent to the Application/Service ones.

**Scripts** - represents any script commands you wish to launch such as: build, kick off Integration Tests etc.

Once you have constructed your topology roughly the way you envisage, you are ready to start configuring each service/scripts.

### Configuring Templates

Starting with Infrastructure, double-click to open up the dialog box as shown:

![alt text](https://raw.githubusercontent.com/gar2000b/discovery-workbench/master/images/service-dialog.png "Instructions")

1. Enter a **'Service Name'**.
2. The **'Startup Command'** should reflect the same command you would use as if launching it from a cmd line terminal. (in windows) although you can target bat files direct, it is recommended that you use cmd /c in front of OS specific commands that only the Windows Command Intepreter would be able to process. Things like rd... Startup command example for zookeeper: **%CONFLUENT_HOME%\bin\windows\zookeeper-server-start.bat %CONFLUENT_HOME%\etc\kafka\zookeeper.properties**
3. **Note**: environment variables in all commands (for all template types) are wrapped in %%.
4. The **'Running Clause'** is a statement that Discovery Workbench will search for once the app is launched. Once it encounters it from **STDIN** (exactly as you would read it from the console), it then determines that the app is now running and then updates it to the **RUNNING** state. You must find an apporpriate running clause in advance. An example for zookeeper might be: **binding to port 0.0.0.0/0.0.0.0:2181** as we would see that if we launched zookeeper from the cmd line.
5. The **'Shutdown Command'** is what you would run if you had any cleanup operations beyond just destroying the service with stop. Zookeeper for example is: **%CONFLUENT_HOME%\bin\windows\zookeeper-server-stop.bat**
6. The **'shutdown command 2'** is a secondary shutdown command for any additional cleanup beyond simply shutting down gracefully + cleanup. In the above case, we are using this with zookeeper to clean up it's log files using: **cmd /c rd /S /Q c:\tmp\zookeeper** - note the use of **cmd /c**.
7. **Port Number** is the main port the application may be bound to. It is optional and will be used more in a future version of this tool for re-binding and force killing apps.
8. Once complete, click **'update'**.
9. Note: It is recommended that you experiment with these commands from a console before wiring this up into Discovery Workbench. It is important that apps get shutdown and cleaned up properly as to not leave it in an inconsistent state.
10. Configuring **Scripts** are easier, simply add a **name + command**.

![alt text](https://raw.githubusercontent.com/gar2000b/discovery-workbench/master/images/scripts-dialog.png "Instructions")

### Updating Start/Stop order

### Change the processing type

### Saving Workspace

## Notes and other stuff...

**TODO**: May need to rethink this section and possibly move it elsewhere...

The scope of this project is to orchestrate and run all aspects of a local/dev copy of a distributed system (typically microservice/cloud native applications although this need not be restricted to this architecture pattern).

In plain English, one should be able to easily start up all of his/her applications from the touch of a button, all from one place in this GUI tool + much much more.

A lot of this work (together with POCs have been done offline since 1st November 2016). As the author (Gary Black), I am passing over this code and integrating it piece by piece over the coming months (as of 09/09/17).

The following list illustrates all of the phase 1 goals of this project:

1. Spin up local services (i.e microservices).  
2. Spin up supporting infrastructure (like DBMSs, queue platforms, config and discovery servers, APIs, you name it).  
3. Execute any kind of script that is supported on the target OS. Ideally it should be able to run .bat and bash scripts.  
4. All of the above should be technology agnostic, i.e if they can run on the intended operating system, then they should all be able to run from within this tool.  
5. Execute all scripts, services, infrastructure from a set list (that can be modified).  
6. ^ items from the above list will have the option to be executed sequentially after the previous item or in parallel with any following items marked 'concurrent'. We'll re-visit this with an example below.  
7. All of this should be produced within the GUIs workspace by a development team/engineer. This will include dragging template elements (microservices, infrastructure, scripts) in from the side-bar onto the main workspace for creating our topology. The tool will have a feature to allow the workspace to be saved as a file for others in the team to get their local environments set up and running quickly using this same tool. It is proposed that this file be kept up to date and saved to a version-control repository for all the team to share.

Beyond just bringing services up/down and executing all myriad of scripts that may for example: clear down DBs, Queues, Indexes, create topics, inject payloads or even execute Integration Tests locally, the real purpose of this project is as follows:

As a Software Engineer building distributed systems, I want a tool that brings backend systems to life via a graphical topology such that it is instantly recognisable to all stakeholders on such projects.

I want to be able to create/drive high-level end-to-end integration tests from within this tool for showcase/demo purposes. I want the GUI diagram/topology to come to life as payloads traverse through the interconnected nodes from one system to another as you could probably imagine in your mind - think of it as a real-time simulation, except it will be real data flowing through real applications running locally on your system.

Again, this will just bring back-end systems development to the forefront in a way that is rarely done to allow teams to report on/demonstrate functional progress as a project evolves.  It is easy to showcase a nice front-end website but what about backend systems that lift, shift and enrich data? Do we really want to continue using cmd line tools that show lots of grouped data and then ask our stakeholders to take our word for it? eh, this worked end to end by the way... It's just hard at times for others to visualise what was achieved during that sprint as demonstrating back-end can be quite dry at times.

To achieve the above, the graphical elements will be enhanced to support detection of payloads as they traverse through the system. All components/paths will come to life in the form of animations that represent that flow of data.  
Specifically, in order to keep this tool language agnostic (example: apps written in .NET, some Java, some Python etc), the data capture of processing/payload movements will be done through a special instance of each applications log files. Yes, this tool will tail log files.

Graphical tools from within the Microservice Orchestrator will be used to create injection/assertion patterns hooked up to the relevant areas within the system/topology.  
Any special drivers/plugins required will be written as use cases for those technologies appear. If this tool becomes useful to other people, this project can be set up in such a way as to allow others to contribute by writing such integration tools.

The following list illustrates all of the phase 2 goals of this project:

1. Shutdown all services on exit of application.  
2. In the event that this application is either forcibly terminated, crashes or is otherwise killed due to unknown factors, the process handles for each of the services will be lost and all running services will in theory still be running and locking out ports etc. It is proposed that smarts are built in to detect rouge applications bound to these ports and have them terminated elegantly, if not force kill. This is for those rare occasions where this may happen.  
3. There will be a feature to allow the end user to create interconnections between the template GUI components (mentioned in phase one). This will allow the animations of payloads to flow through the system.  
3. Graphical tool box templates will allow the end user to specify insertion points of data payloads. The initial one will be REST insertion. Other plugins/integrations can come later.  
4. Graphical tool box templates will allow the end user to specify assertion points of data payloads. The initial one will is yet to be determined. Other plugins/integrations can come later.  
5. Graphical tool box templates will allow the end user to specify GUI listeners for the end user to monitor payloads at any of the junctions between services (where there are logs of course).  
6. The end user should have the ability to inject random payloads manually from the insertion tool box, capturing the data at all of the listener and assertion tool boxes.  
7. The end user should have the ability to fire pre-determined load via an insertion tool box and monitor performance metrics at the assertion toolbox.

The core graphics libraries are libGDX.

It was decided that this be set up as a maven project as this allows one to integrate in spaces not equipped to execute gradle.

libGDX has been set up with gradle in mind. It doesn't natively support maven from the gdx-setup.jar tool. When you set up a project, it is created as a gradle project.

Luckily they have created a maven architype for setting up skeleton libGDX projects here: https://github.com/libgdx/libgdx-maven-archetype

^ you'll find instructions on this page. Note: this is a one time setup for new libGDX projects and is here for reference only. The more usefull parts are in how to run the app or to package it up as a jar.

Note: a pass thorugh of the main pom was required to update the project to the latest version of libGDX.
...
