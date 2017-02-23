# introsde-2016-assignment-1
Introsde-2016-assignment-1 report
Davide Lissoni Mat.179878
24/10/2016


## 1.Introduction:

This report is about the first assignment  of the Introduction to Service Design and Engineering
course.
The request for this project was to extend the Health Profile Reader/Writer application, implemented during the course laboratory lessons, in the following way:

1. **Modify the database**: use an xml document as database instead of the HashMap of a Pojo  used in the laboratory example.The database contains basic information about a person an his/her health profile;
2. **Extend the HealthProfile class** by adding the methods getHeight, getWeight and getHelathProfile that given the id of a person as input returns respective his/her weight ,height and health profile. Again a method that return all the people information present in the DB and finally a function that, given an operator and a weight number as inputs returns all the person that satisfied the condition indicated. Note that the database has been change in a xml file, therefore these methods need to use xpath technology;
3. **Create an xsd schema of the database.xml** in order to generate java JAXB  classes through XJC;
4. **Implements two classes that does the marshalling** of the classes just generated in order to transform their memory representation in an xml and json files;
5. **Implements a class that does the un-marshalling** of the database.xml and use as data tree representation the JAXB classes generated.

Furthermore the project  must be entirely compilable and executable through ant (I used Apache ivy as dependency manager).


## 2.Implementation:

In this chapter will be exposed how the application has been implemented, explaining class to class their functionalities and  their code.

### 2.1 HealthProfile.java:

This class is responsible for parse and query the database.xml in order to retrieve the information listed above.
The first feature then, is to load, in order to parse it, the xml file, by using the DocumentBuilder(DocumentBuliderFactory instance) parse(File) method,  as explained in the laboratory lessons. 
The next task was query the database in order to get the information required. This feature has been managed through xpath expressions on an XPAthOBject, executed on the DocumentBuilder.parse(database.xml). An example is showed here :
```java
 //get all the people according to an operator and the weight
	  public NodeList getPeopleAccordingWeight(String weight,String operator)
			  throws XPathExpressionException{
		  XPathExpression expr = xpath.compile(
				  "/people/peopleList/person[healthprofile/weight"+operator+weight+"]");
	      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	      return nodes;
		 }
	  
```

All the “query” functions implemented return a Node elements that will be in turn parsed (using Node.getChildNode().item().getNodeName and Node.getChildNode().item().getTextContent) in the main(), in order to print out the information requested in a formatted String.

### 2.2 XSD schema and JAXB classes generation.

The xsd schema created represent respectively the database.xml that in this project has been called “people.xml”. 
The java classes has been generated using xjc and implemented by adding the following lines on the build.xml:
```xml
<target name="generate" depends="init">
		<taskdef name="xjc" classname="com.sun.tools.xjc.XJCTask" classpathref="lib.path.id">
		</taskdef>
		<xjc schema="people.xsd" destdir="${src.dir}" package="${xjc.package}" />
	</target>
```
This method generates a new package (healthprofile.generated) containing as many classes as many different complexTypes are present in the people.xsd. In our case four, which are:
PeopleType, PeopleListType, PersonType and HelathProfileType. 
Furthermore xjc generate as default the ObjectFactory class which contains factory methods for each Java content interface and Java element interface generated.
Since  xsd schema has a tree data structure, also the JAXB classes keep the same “skeleton” where PeopleType represent the tree root.


### 2.3 JAXBMarshaller.java: 

This class is the xml marshaller, which has the task of does the marshall of data contained using JAXB Classes generated before, representing them in a xml view. 
Note that the data used as example for this project are setted during the execution of this class and represent three different persons. 
This data can be changed only by modifyng the project code.
For JAXB marshalling and un-marshalling operation has been used the jaxb-api contained in javax.xml.bind library.

The class starts creating a new jaxbMarshaller which will generate a new xml File.
After that will be initialized the marshaller and then will be setted its output properites:
```java
//initialize marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			healthprofile2.generated.ObjectFactory factory =
					new healthprofile2.generated.ObjectFactory();
```

The next feature implemented in this class is the settings of the three different persons mentioned above and their relative information, using the JAXB Classes as “data container” .  This has been possible with the help of the ObjectFactory class which contains the create method of the JAXB classes. 
The person information has been inserted using the set methods of the auto-generated classes, respecting the previous xml tree structure as showed below:
```java
//first person
			PersonType person = factory.createPersonType();
			person.setBirthdate("24/11/92");
			person.setFirstname("Davide");
			person.setId(new Integer(1).shortValue());
			person.setLastname("Lissoni");
			HealthprofileType healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
```	

Finally the java class root just created by the factory and setted (in our case PeopleType),will be put in a JAXBElement and then passed to the marshaller that will does the marshall of it. 
The result will be printed in the file created at the beginning of the JAXBMarshaller main();

### JAXBMarshallerJSon.java

This class is the json marshaller and keep the same structure of JAXBMarshaller. This it has been possible thanks to the org.eclipse.persistence.jaxb.moxy library which enables Java developers to efficiently bind Java classes to xml and also json schema.
The only difference between the two Marshaller classes is the different marshaller properties settings:
```java
//set json marshaller propertes
      System.setProperty("javax.xml.bind.context.factory","org.eclipse.persistence.jaxb.JAXBContextFactory");			
			JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,"application/json");
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			healthprofile2.generated.ObjectFactory factory = new healthprofile2.generated.ObjectFactory();
```	

### 2.4 JAXBUnMarshaller.java

This class is in charge to de-serialize XML data into newly created Java content trees that, in our case, is composed by the JAXB classes.
Also the unMarshaller class works with the help of the jaxb-api.
JAXBUnMarshaller starts setting the file that we want to unmarshall(people.xml), then initialize the un-marshaller and set the schema that it has to handle(people.xsd).
```java
	JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			//initialize unmarshaller
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			// set the schema xsd for unmarsller
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File("people.xsd"));
			unMarshaller.setSchema(schema);
```	

The class is supported by another class CustomValidationEventHandler that check and handle possible errors and issues came out during the un-marshalling. CustomValidationEventHandler uses a handler called ValidationEventHandler. This handler represent an instance of ValidationEvent.

CustomValidationEventHandler is initialized at this point, after have setted the un-marshaller schema.
The next step for this class is to do the un-marshaller and put it in a JAXBElement as illustrated here:
  ```java
    // start unmarshaller
			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);
			//get out people in the xml 
			PeopleType people = peopleElement.getValue();
```	

Finally, in order to show that the un-marshaller has been succesfully completed, I decided to print out the people information that come from  JAXBElement through the get method of the JAXB classes.

### 2.5 ANT and Apache Ivy

In order to compile and execute the project using ANT, there is the need of an ANT build script: build.xml.
For this project has been modified and used the build.xml utilized in the LAb04 session, updating it according to this assignments needs.
The ivy.xml, containing the project dependencies, is also the same utilized in the lab04 session modified in order to not download unnecessary library and add  org.eclipse.persistence.jaxb.moxy used in the JAXBMarshallerJson.class


## 3.Deployment:

In order to run the project through command line make sure to be located in the project folder.
Initially there is the need to compile the application before execute it.
To compile the program type the command :
```sh
ant compile
```
This command will download Ivy, install it and then download the dependencies present on the Ivy.xml.
This command also start the generate that will generete the JAXB classes. 

Now the project is ready to be executed.
In order to execute the application there are various command  according to what the user want to run.
To execute all the main classes present in the project type:
```sh
ant execute.evalutation
```
This command will run HealthProfile.main(),JAXBMArshaller.main(), JAXBMarshallerJson.main() and JAXBUnMarshaller.main() and print out all the results.

In order to run just one of this main type
```sh
ant execute.<Class name>
```
Note that for this assignment has been asked to run the application calling only specifics methods using specific inputs. Therefore the code is setted to :

- Print out all the people present in the database;
- Print out the health profile of the person with id=5;
- Print out all the people having weight > 90;
- Run JAXBMarshaller and generate peopleGenerated.xml
- Run JAXBMarshallerJSON and generate peopleGenerated.json
- Run JAXBUnMarshaller and print out the result.
