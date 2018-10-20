# Discovery Workbench

**PROJECT START DATE**: 1st November 2016

**LICENSE**: https://github.com/gar2000b/discovery-workbench/blob/master/LICENSE.md

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/social-insurance-workflow-service.png "Social Insurance Workflow")

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

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/open-dialog.png "Open Social Insurance Workflow Service")

## Provisioning Instructions

Currently, the provisioning functionality is under development, so the following instructions are for manually provisioning your infrastructure:

1. Once you have opened up a workspace, the first thing to do is to click on 'Instructions' to see how to install and configure any pre-requisites required for your project. It should contain information like installing Elastsearch and setting environment variables.
2. Once you have gone through the instructions, shutdown, restart the workbench and open the project back up - this is so that it picks up on any env vars you may have set. **Note**: env vars will be getting overridden with internal config vars soon, then you will not have to do this in future.
3. You should now be good to run and spin up all your services.

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/instructions-dialog.png "Instructions")

## Running your services

The project should already have been pre-configured (to one extent or the other) by another member of your team and will be ready to be executed after going through the instructions above (this should be a one time thing).

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

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/service-dialog.png "Instructions")

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

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/scripts-dialog.png "Scripts")

### Updating Start/Stop order

Refering again to the first screenshot, you will notice that every time you add either a new service or infrastructure item, it gets added to the startup list on the right-hand side.

You can adjust the startup order of this list by simply selecting the service and then clicking the up/down buttons above.

### Change the processing type

The Discovery Workbench has this concept of processing type for each service that spins up. Basically it dictates whether the service spins up **sequentially** or in **parallel**.

When you select an item from the list and then click **'Toggle Processing Type'** you will notice the the left-hand side of the items label toggles between **SEQ (sequential)** and **PAR (parallel)**.

**Sequential** basically means that the service will spin up on it's own. The sebsequent service in the list will not spin up until the current service is in the running state.

**Parallel** basically means that the subsequent service is free to spin up concurrently at the same time as the current service. This can continue until it hits another service marked as **SEQ**.

^ this is especially useful when we have service dependencies on one another. For example, spinning up a lot of our infrastructure makes sense to start up sequentially as they are often dependencies of our services (and sometimes of each other). Services/Applications themselves can usually startup in parallel with each other.

### Saving Workspace

Saving the workspace is as simple as clicking the **'Save'** button.

![alt text](https://github.com/gar2000b/discovery-workbench/blob/master/images/save-workspace-dialog.png "Save workspace")

Simply enter the path to the file to wish to save, e.g: **C:\work\ms-orchestrator\workspaces\deposit-transaction-eod-flow**

Click **'Save'**

Actually, since we are intending (for the moment to share this via our git repo), please ensure that it is saved within the repo's workspaces directory, git add, commit and push back there. A future version will include a 'Discovery Workbench Service' that will make sharing workspaces even easier.

## Where does this sit on the Path to Production?

A - Right at the beginning. Actually before we even push any of our stuff out to our pipelines. It is purely used to aid the developer in the production and testing of software locally on their machine. Again, it keeps everything in one place and once set up, should make life much easier for engineers to orchestrate all these CI/CD pre-pipeline activities.

## Future

This project is a work in progress. This first pass of the tool is now considered MVP ready. It will naturally go through a period of hardening over the coming months. There are multiple phases earmarked for the future. Will document as soon as possible.

**FUTURE PHASES**: https://github.com/gar2000b/discovery-workbench/blob/master/FUTURE.md
