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

Phase 3 and beyond:

This involves cloudification. One of the main goals is to port everything over to become a graphical single page web application (employing javascript/html5). This will include all of the functionality of the previous workbench + provide all of the monitoring hooks into the app using things like Springs actuator (if Java app) to health check metrics (however, done in a generic way).
The other main goal is to have the apps themselves be deployed to various cloud environments and provide generic functionality to orchestrate them (start/stop), monitoring including tracking of payloads via logs etc... A lot of this is going to require a fair bit of design + test.

The core graphics libraries are libGDX.

It was decided that this be set up as a maven project as this allows one to integrate in spaces not equipped to execute gradle.

libGDX has been set up with gradle in mind. It doesn't natively support maven from the gdx-setup.jar tool. When you set up a project, it is created as a gradle project.

Luckily they have created a maven architype for setting up skeleton libGDX projects here: https://github.com/libgdx/libgdx-maven-archetype

^ you'll find instructions on this page. Note: this is a one time setup for new libGDX projects and is here for reference only. The more usefull parts are in how to run the app or to package it up as a jar.

Note: a pass thorugh of the main pom was required to update the project to the latest version of libGDX.
...
