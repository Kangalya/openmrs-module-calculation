/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.calculation;

import java.util.Map;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.api.patient.PatientCalculationContext;
import org.openmrs.calculation.api.patient.PatientCalculationService;
import org.openmrs.calculation.evaluator.patient.PatientCalculationEvaluator;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.provider.ClasspathCalculationProvider;
import org.openmrs.calculation.result.CohortResult;

/**
 * Calculation using results of {@link InnerCalculation}.
 */
@Handler(supports = { OuterCalculation.class }, order = 50)
public class OuterCalculation extends BaseCalculation implements PatientCalculation, PatientCalculationEvaluator {
	
	/**
	 * @see org.openmrs.calculation.evaluator.patient.PatientCalculationEvaluator#evaluate(org.openmrs.Cohort,
	 *      org.openmrs.calculation.patient.PatientCalculation, java.util.Map,
	 *      org.openmrs.calculation.api.patient.PatientCalculationContext)
	 */
	@Override
	public CohortResult evaluate(Cohort cohort, PatientCalculation calculation, Map<String, Object> parameterValues,
	                             PatientCalculationContext context) {
		try {
			PatientCalculation calc = (PatientCalculation) new ClasspathCalculationProvider().getCalculation(
			    InnerCalculation.class.getName(), null);
			
			Context.getService(PatientCalculationService.class).evaluate(1, calc);
		}
		catch (InvalidCalculationException e) {
			throw new RuntimeException(e);
		}
		
		Concept concept = Context.getConceptService().getConcept(3);
		if (!concept.isRetired()) {
			throw new RuntimeException("Concept should have been retired by innter calculation");
		}
		
		return new CohortResult();
	}
	
}
