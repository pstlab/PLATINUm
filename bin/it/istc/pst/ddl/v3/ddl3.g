grammar ddl3;

options {
    backtrack=true;
    memoize=true;
    output=AST;
}

ddl	:	domain (problem)? ->^('DOMAIN' domain (problem)?) | problem ->^('PROBLEM' problem);

domain	
	:	'DOMAIN' ID '{' temporal_module domain_element* '}' ->^(ID<DDLDomain> temporal_module domain_element*);

temporal_module
	:	'TEMPORAL_MODULE' ID '=' range ',' INT ';' ->^(ID<DDLTemporalModule> range INT);

domain_element
	:	parameter_type | component_type | component | timeline_synchronization;

// Parameter types
parameter_type
	:	numeric_parameter_type | enumeration_parameter_type;
	
numeric_parameter_type
	:	'PAR_TYPE' 'NumericParameterType' ID '=' '[' number ',' number ']' ';' ->^(ID<DDLNumericParameterType> number number);
	
enumeration_parameter_type
	:	'PAR_TYPE' 'EnumerationParameterType' ID '=' '{' ID (',' ID)* '}' ';' ->^(ID<DDLEnumerationParameterType> ID*);

// Temporal relations
temporal_relation_type
	:	'MEETS' ->^('MEETS'<DDLTemporalRelationType>)
	|	'MET-BY' ->^('MET-BY'<DDLTemporalRelationType>)
	|	'BEFORE' range ->^('BEFORE'<DDLTemporalRelationType> range)
	|	'AFTER' range ->^('AFTER'<DDLTemporalRelationType> range)
	|	'EQUALS' ->^('EQUALS'<DDLTemporalRelationType>)
	|	'STARTS' ->^('STARTS'<DDLTemporalRelationType>)
	|	'STARTED-BY' ->^('STARTED-BY'<DDLTemporalRelationType>)
	|	'FINISHES' ->^('FINISHES'<DDLTemporalRelationType>)
	|	'FINISHED-BY' ->^('FINISHED-BY'<DDLTemporalRelationType>)
	|	'DURING' range range ->^('DURING'<DDLTemporalRelationType> range range)
	|	'CONTAINS' range range ->^('CONTAINS'<DDLTemporalRelationType> range range)
	|	'OVERLAPS' range ->^('OVERLAPS'<DDLTemporalRelationType> range)
	|	'OVERLAPPED-BY' range ->^('OVERLAPPED-BY'<DDLTemporalRelationType> range)
	|	'STARTS-AT' ->^('STARTS-AT'<DDLTemporalRelationType>)
	|	'ENDS-AT' ->^('ENDS-AT'<DDLTemporalRelationType>)
	|	'AT-START' ->^('AT-START'<DDLTemporalRelationType>)
	|	'AT-END' ->^('AT-END'<DDLTemporalRelationType>)
	|	'BEFORE-START' range ->^('BEFORE-START'<DDLTemporalRelationType> range)
	|	'AFTER-END' range ->^('AFTER-END'<DDLTemporalRelationType> range)
	|	'START-START' range ->^('START-START'<DDLTemporalRelationType> range)
	|	'START-END' range ->^('START-END'<DDLTemporalRelationType> range)
	|	'END-START' range ->^('END-START'<DDLTemporalRelationType> range)
	|	'END-END' range ->^('END-END'<DDLTemporalRelationType> range)
	|	'CONTAINS-START' range range ->^('CONTAINS-START'<DDLTemporalRelationType> range range)
	|	'CONTAINS-END' range range ->^('CONTAINS-END'<DDLTemporalRelationType> range range)
	|	'STARTS-DURING' range range ->^('STARTS-DURING'<DDLTemporalRelationType> range range)
	|	'ENDS-DURING' range range ->^('ENDS-DURING'<DDLTemporalRelationType> range range);

// Parameter constraints
parameter_constraint
	:	numeric_parameter_constraint | enumeration_parameter_constraint;
	
numeric_parameter_constraint
	:	numeric_comparison_lvalue '=' numeric_comparison_rvalue ->^('='<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue)
	|	numeric_comparison_lvalue '<' numeric_comparison_rvalue ->^('<'<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue)
	|	numeric_comparison_lvalue '>' numeric_comparison_rvalue ->^('>'<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue)
	|	numeric_comparison_lvalue '<=' numeric_comparison_rvalue ->^('<='<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue)
	|	numeric_comparison_lvalue '>=' numeric_comparison_rvalue ->^('>='<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue)
	|	numeric_comparison_lvalue '!=' numeric_comparison_rvalue ->^('!='<DDLNumericParameterConstraint> numeric_comparison_lvalue numeric_comparison_rvalue);

numeric_comparison_lvalue
	:	VarID | number '*'^ VarID;

numeric_comparison_rvalue
	:	first_numeric_comparison_rvalue (other_numeric_comparison_rvalues)*;

first_numeric_comparison_rvalue
	:	VarID | number '*'^ VarID | number;

other_numeric_comparison_rvalues
	:	('+' | '-')^ numeric_comparison_value;

numeric_comparison_value
	:	VarID | INT '*'^ VarID | INT;

enumeration_parameter_constraint
	:	VarID '=' enumeration_comparison_rvalue ->^('='<DDLEnumerationParameterConstraint> VarID enumeration_comparison_rvalue) | VarID '!=' enumeration_comparison_rvalue ->^('!='<DDLEnumerationParameterConstraint> VarID enumeration_comparison_rvalue);
	
enumeration_comparison_rvalue
	:	VarID | ID;

// Component types:
component_type
	:	simple_ground_state_variable_component_type | singleton_state_variable_component_type | renewable_resource_component_type | consumable_resource_component_type;

// SimpleGroundStateVariable
simple_ground_state_variable_component_type
	:	'COMP_TYPE' 'SimpleGroundStateVariable' ID '(' simple_ground_state_variable_component_decision_type (',' simple_ground_state_variable_component_decision_type)* ')' '{' (simple_ground_state_variable_transition_constraint)* '}' ->^(ID<DDLSimpleGroundStateVariableComponentType> (simple_ground_state_variable_component_decision_type)+ (simple_ground_state_variable_transition_constraint)*);

simple_ground_state_variable_component_decision_type
	:	ID '(' ')' ->^(ID<DDLSimpleGroundStateVariableComponentDecisionType>);

simple_ground_state_variable_transition_constraint
	:	'VALUE' simple_ground_state_variable_component_decision range ('MEETS' '{' (simple_ground_state_variable_transition_element ';')* '}')? ->^('VALUE'<DDLSimpleGroundStateVariableTransitionConstraint> simple_ground_state_variable_component_decision range (simple_ground_state_variable_transition_element)*);

simple_ground_state_variable_transition_element
	:	simple_ground_state_variable_component_decision;

// SingletonStateVariable
singleton_state_variable_component_type
	:	'COMP_TYPE' 'SingletonStateVariable' ID '(' singleton_state_variable_component_decision_type (',' singleton_state_variable_component_decision_type)* ')' '{' (singleton_state_variable_transition_constraint)* '}' ->^(ID<DDLSingletonStateVariableComponentType> (singleton_state_variable_component_decision_type)+ (singleton_state_variable_transition_constraint)*);

singleton_state_variable_component_decision_type
	:	ID '(' (ID (',' ID)*)? ')' ->^(ID<DDLSingletonStateVariableComponentDecisionType> ID*);

singleton_state_variable_transition_constraint
	:	'VALUE' singleton_state_variable_component_decision range 'MEETS' '{' (singleton_state_variable_transition_element ';')* '}' ->^('VALUE'<DDLSingletonStateVariableTransitionConstraint> singleton_state_variable_component_decision range (singleton_state_variable_transition_element)*);

singleton_state_variable_transition_element
	:	singleton_state_variable_component_decision | parameter_constraint;

// Renewable resource
renewable_resource_component_type
	:	'COMP_TYPE' 'RenewableResource' ID '(' positive_number ')' ->^(ID<DDLRenewableResourceComponentType> positive_number);

// Consumable resource
consumable_resource_component_type
	:	'COMP_TYPE' 'ConsumableResource' ID '(' positive_number ',' positive_number ')' ->^(ID<DDLConsumableResourceComponentType> positive_number positive_number);

// Components
component
	:	'COMPONENT' ID '{' (timeline)* '}' ':' ID ';'->^(ID<DDLComponent> ID (timeline)*);

timeline
	:	'ESTA_LIGHT' ID '(' (parameter (',' parameter)*)? ')' ->^('ESTA_LIGHT'<DDLTimeline> ID parameter*) | 'BOUNDED' ID '(' (parameter (',' parameter)*)? ')' ->^('BOUNDED'<DDLTimeline> ID parameter*) | 'FLEXIBLE' ID '(' (parameter (',' parameter)*)? ')' ->^('FLEXIBLE'<DDLTimeline> ID parameter*) | 'ESTA_LIGHT_MAX_CONSUMPTION' ID '(' (parameter (',' parameter)*)? ')' ->^('ESTA_LIGHT_MAX_CONSUMPTION'<DDLTimeline> ID parameter*);

// Synchronizations

timeline_synchronization
	:	'SYNCHRONIZE' ID '.' ID '{' (synchronization)+ '}' ->^('SYNCHRONIZE'<DDLTimelineSynchronization> ID ID (synchronization)+);

synchronization
	:	'VALUE' component_decision '{' (synchronization_element)* '}' ->^('VALUE'<DDLSynchronization> component_decision (synchronization_element)*);

component_decision
	:	simple_ground_state_variable_component_decision | singleton_state_variable_component_decision | renewable_resource_component_decision | consumable_resource_component_decision;

instantiated_component_decision
	:	ID ('<' parameter (',' parameter)* '>')? ID '.' ID '.' component_decision ('AT' range range range)? ->^(ID<DDLInstantiatedComponentDecision> ID ID component_decision (parameter)* (range range range)?);
	
parameter
	:	ID | '!' | '?' | 'c' | 'u';

simple_ground_state_variable_component_decision
	:	('<' parameter (',' parameter)* '>')? ID '(' ')' ->^(ID<DDLSimpleGroundStateVariableComponentDecision> parameter*);
	
singleton_state_variable_component_decision
	:	('<' parameter (',' parameter)* '>')? ID '(' (par_value (',' par_value)*)? ')' ->^(ID<DDLSingletonStateVariableComponentDecision> par_value* '(' parameter*);
	
renewable_resource_component_decision
	:	('<' parameter (',' parameter)* '>')? 'REQUIREMENT' '(' par_value ')' ->^('REQUIREMENT'<DDLRenewableResourceComponentDecision> par_value parameter*);
		
consumable_resource_component_decision
	:	consumable_resource_production_component_decision | consumable_resource_consumption_component_decision;
		
consumable_resource_production_component_decision
	:	('<' parameter (',' parameter)* '>')? 'PRODUCTION' '(' par_value ')' ->^('PRODUCTION'<DDLConsumableResourceComponentDecision> par_value parameter*);
		
consumable_resource_consumption_component_decision
	:	('<' parameter (',' parameter)* '>')? 'CONSUMPTION' '(' par_value ')' ->^('CONSUMPTION'<DDLConsumableResourceComponentDecision> par_value parameter*);

par_value
	:	VarID ('=' number)? ->^(VarID number?) | VarID ('=' ID)? ->^(VarID ID?);

synchronization_element
	:	instantiated_component_decision ';' | parameter_constraint ';' | (from=ID)? temporal_relation_type to=ID ';' ->^(';'<DDLTemporalRelation> temporal_relation_type $to $from?);

problem	:	'PROBLEM' ID '(' 'DOMAIN' ID ')' '{' problem_element* '}' ->^(ID<DDLProblem> ID problem_element*);

problem_element
	:	instantiated_component_decision ';' | from=ID temporal_relation_type to=ID ';' ->^(';'<DDLTemporalRelation> temporal_relation_type $to $from?) | parameter_constraint ';';

range	:	'[' positive_number ',' positive_number ']' ->^('['<DDLRange> positive_number positive_number);

positive_number
	:	('+')? INT ->^(INT) | ('+')? 'INF' ->^('INF');

number	:	(sign)? INT ->^(INT (sign)?) | (sign)? 'INF' ->^('INF' (sign)?);

sign	:	'+' | '-';

ID	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|'@')*;

VarID	:	'?' ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|'@')*;

INT	:	'0'..'9'+;

COMMENT	:	'//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;} | '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;};

WS	:	( ' ' | '\t' | '\r' | '\n' ) {$channel=HIDDEN;};