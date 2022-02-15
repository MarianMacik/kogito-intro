package org.kie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.ProcessInstanceReadMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KogitoApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LoanApplicationProcessTest {
    
    @Autowired
    @Qualifier("LoanApplication")
    Process<? extends Model> loanProcess;

    @BeforeEach
    public void setup() {
        loanProcess.instances().values(ProcessInstanceReadMode.MUTABLE).forEach(pi -> pi.abort());
    }

    @Test
    public void testLoanProcessApproved() {
        assertNotNull(loanProcess);

        Model m = loanProcess.createModel();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("amount", 4999);
        m.fromMap(parameters);

        ProcessInstance<?> processInstance = loanProcess.createInstance(m);
        processInstance.start();

        assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.status());
        Model result = (Model) processInstance.variables();
        assertEquals(2, result.toMap().size());
        assertEquals(4999, (result.toMap().get("amount")));
        assertTrue((boolean)(result.toMap().get("approved")));

        // no active process instances
        assertEquals(0, loanProcess.instances().size());
    }

    @Test
    public void testLoanProcessDeclined() {
        assertNotNull(loanProcess);

        Model m = loanProcess.createModel();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("amount", 5000);
        m.fromMap(parameters);

        ProcessInstance<?> processInstance = loanProcess.createInstance(m);
        processInstance.start();

        assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.status());
        Model result = (Model) processInstance.variables();
        assertEquals(2, result.toMap().size());
        assertEquals(5000, (result.toMap().get("amount")));
        assertFalse((boolean)(result.toMap().get("approved")));

        // no active process instances
        assertEquals(0, loanProcess.instances().size());
    }

}
