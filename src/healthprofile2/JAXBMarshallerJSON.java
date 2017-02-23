package healthprofile2;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import healthprofile2.generated.HealthprofileType;
import healthprofile2.generated.PeopleListType;
import healthprofile2.generated.PeopleType;
import healthprofile2.generated.PersonType;
//class used to marshall (create json document) from value pre-setted.  The schema follow is given by the auto-generated classes
// using JAXB , following the people.xsd schema
// generate (peopleGenerated.json)
public class JAXBMarshallerJSON 
{
	public static void generateJSONDocument() throws DatatypeConfigurationException {
		try {
			//set json marshaller propertes
			System.setProperty("javax.xml.bind.context.factory","org.eclipse.persistence.jaxb.JAXBContextFactory");			
			JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,"application/json");
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			healthprofile2.generated.ObjectFactory factory = new healthprofile2.generated.ObjectFactory();

			// create json schema 
			PeopleType people = factory.createPeopleType();
			PeopleListType peopleList=factory.createPeopleListType();
			people.setPeopleList(peopleList);
			
			//first person example
			PersonType person = factory.createPersonType();
			List<PersonType> personList = peopleList.getPerson();
			person.setBirthdate("24/11/92");
			person.setFirstname("Davide");
			person.setId(new Integer(1).shortValue());
			person.setLastname("Lissoni");
			HealthprofileType healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			healthprofile.setHeight(180);
			healthprofile.setWeight(70);
		    GregorianCalendar c = new GregorianCalendar();
		    c.setTime(new Date());
		    XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);
			
			//second person
			person = factory.createPersonType();
			person.setBirthdate("17/02/63");
			person.setFirstname("Micheal");
			person.setId(new Integer(2).shortValue());
			person.setLastname("Jordan");
			healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			healthprofile.setHeight(198);
			healthprofile.setWeight(90);
		     c = new GregorianCalendar();
		    c.setTime(new Date());
		    date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);
			
			//third person
			person = factory.createPersonType();
			person.setBirthdate("14/03/88");
			person.setFirstname("Stephen");
			person.setId(new Integer(3).shortValue());
			person.setLastname("Curry");
			healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			healthprofile.setHeight(191);
			healthprofile.setWeight(93);
		     c = new GregorianCalendar();
		    c.setTime(new Date());
		    date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);

			
			
			
			
			String jsonDocument = "peopleGenerated.json";
			//populate the json document and start marshaller
			JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
			marshaller.marshal(peopleElement,new File(jsonDocument));
			
	        
	       
			
	       
	        
		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}

	public static void main(String[] argv) throws DatatypeConfigurationException {
		 generateJSONDocument();
		 System.out.println("peopleGenerate.json created");
	}
}
