package com.makolab.rnd.mammoth.ramt;

import com.makolab.rnd.mammoth.model.rdf.Object;
import com.makolab.rnd.mammoth.model.rdf.Predicate;
import com.makolab.rnd.mammoth.model.rdf.Subject;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.model.rdf.impl.BlankNode;
import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.model.rdf.impl.Literal;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class Rdf4jMapper {

  private ValueFactory valueFactory;

  public Rdf4jMapper() {
    this.valueFactory = SimpleValueFactory.getInstance();
  }

  public Triple statementToTriple(Statement statement) {
    Subject subject;
    if (statement.getSubject() instanceof IRI) {
      subject = new Iri(statement.getSubject().stringValue());
    } else {
      // If not an IRI, then subject has to be a blank node.
      BNode bNode = (BNode) statement.getSubject();
      subject = new BlankNode(bNode.getID());
    }

    Predicate predicate = new Iri(statement.getPredicate().stringValue());

    Object object;
    if (statement.getObject() instanceof IRI) {
      object = new Iri(statement.getObject().stringValue());
    } else if (statement.getObject() instanceof org.eclipse.rdf4j.model.Literal) {
      org.eclipse.rdf4j.model.Literal literal = (org.eclipse.rdf4j.model.Literal) statement.getObject();
      String lexicalForm = literal.getLabel();
      if (literal.getLanguage().isPresent()) {
        object = new Literal(lexicalForm, literal.getLanguage().get());
      } else {
        object = new Literal(lexicalForm, new Iri(literal.getDatatype().stringValue()));
      }
    } else {
      // If neither an IRI nor a literal, then object has to be a blank node.
      object = new BlankNode(statement.getObject().stringValue());
    }

    return new Triple(subject, predicate, object);
  }

  public Set<Triple> modelToTriples(Model model) {
    Set<Triple> triples = new HashSet<>();

    for (Statement statement : model) {
      triples.add(statementToTriple(statement));
    }

    return triples;
  }

  public Model triplesToModel(Set<Triple> triples) {
    Model model = new TreeModel();

    for (Triple triple : triples) {
      model.add(tripleToStatement(triple));
    }

    return model;
  }

  public Statement tripleToStatement(Triple triple) {
    Resource subject;
    if (triple.getSubject().isIri()) {
      subject = valueFactory.createIRI(triple.getSubject().valueAsString());
    } else {
      // If not an IRI, then subject has to be a blank node.
      subject = valueFactory.createBNode(triple.getSubject().valueAsString());
    }

    IRI predicate = valueFactory.createIRI(triple.getPredicate().valueAsString());

    Value object;
    if (triple.getObject().isBlankNode()) {
      object = valueFactory.createBNode(triple.getObject().valueAsString());
    } else if (triple.getObject().isIri()) {
      object = valueFactory.createIRI(triple.getObject().valueAsString());
    } else {
      // If not a blank node or an IRI, then object has to be a literal.
      Literal literal = triple.getObject().asLiteral();
      if (literal.getLanguageTag().isPresent()) {
        object = valueFactory.createLiteral(literal.getLexicalForm(), literal.getLanguageTag().get());
      } else {
        object = valueFactory.createLiteral(literal.getLexicalForm(),
            valueFactory.createIRI(literal.getDatatype().valueAsString()));
      }
    }

    return valueFactory.createStatement(subject, predicate, object);
  }

  public String modelToSerialization(Model model, RDFFormat rdfFormat) {
    Writer writer = new StringWriter();
    Rio.write(model, writer, rdfFormat);
    return writer.toString();
  }
}
