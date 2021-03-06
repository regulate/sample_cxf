/**
 * This class is generated by jOOQ
 */
package cxf.sample.persistence.schema.tables;


import cxf.sample.persistence.schema.CxfSample;
import cxf.sample.persistence.schema.Keys;
import cxf.sample.persistence.schema.tables.records.PersonRecord;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.4"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Person extends TableImpl<PersonRecord> {

	private static final long serialVersionUID = 1427226867;

	/**
	 * The reference instance of <code>cxf_sample.person</code>
	 */
	public static final Person PERSON = new Person();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<PersonRecord> getRecordType() {
		return PersonRecord.class;
	}

	/**
	 * The column <code>cxf_sample.person.id</code>.
	 */
	public final TableField<PersonRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>cxf_sample.person.first_name</code>.
	 */
	public final TableField<PersonRecord, String> FIRST_NAME = createField("first_name", org.jooq.impl.SQLDataType.VARCHAR.length(25).nullable(false), this, "");

	/**
	 * The column <code>cxf_sample.person.last_name</code>.
	 */
	public final TableField<PersonRecord, String> LAST_NAME = createField("last_name", org.jooq.impl.SQLDataType.VARCHAR.length(25).nullable(false), this, "");

	/**
	 * The column <code>cxf_sample.person.birth_date</code>.
	 */
	public final TableField<PersonRecord, Date> BIRTH_DATE = createField("birth_date", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

	/**
	 * Create a <code>cxf_sample.person</code> table reference
	 */
	public Person() {
		this("person", null);
	}

	/**
	 * Create an aliased <code>cxf_sample.person</code> table reference
	 */
	public Person(String alias) {
		this(alias, PERSON);
	}

	private Person(String alias, Table<PersonRecord> aliased) {
		this(alias, aliased, null);
	}

	private Person(String alias, Table<PersonRecord> aliased, Field<?>[] parameters) {
		super(alias, CxfSample.CXF_SAMPLE, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<PersonRecord, Long> getIdentity() {
		return Keys.IDENTITY_PERSON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<PersonRecord> getPrimaryKey() {
		return Keys.KEY_PERSON_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<PersonRecord>> getKeys() {
		return Arrays.<UniqueKey<PersonRecord>>asList(Keys.KEY_PERSON_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Person as(String alias) {
		return new Person(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Person rename(String name) {
		return new Person(name, null);
	}
}
