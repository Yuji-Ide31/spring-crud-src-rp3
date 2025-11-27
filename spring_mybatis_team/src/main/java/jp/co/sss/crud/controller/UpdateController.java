package jp.co.sss.crud.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.crud.entity.Department;
import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.form.EmployeeForm;
import jp.co.sss.crud.service.SearchForDepartmentByDeptIdService;
import jp.co.sss.crud.service.SearchForEmployeesByEmpIdService;
import jp.co.sss.crud.service.UpdateEmployeeService;
import jp.co.sss.crud.util.BeanCopy;
import jp.co.sss.crud.util.Constant;

@Controller
public class UpdateController {
	@Autowired
	UpdateEmployeeService updateEmployeeService;

	@Autowired
	SearchForEmployeesByEmpIdService searchForEmployeesByEmpIdService;

	@Autowired
	SearchForDepartmentByDeptIdService searchForDepartmentByDeptIdService;

	@RequestMapping(path = { "/update/input" }, method = { RequestMethod.GET })
	public String inputUpdate(Integer empId, @ModelAttribute EmployeeForm employeeForm) {
		BeanCopy.copyEntityToForm(this.searchForEmployeesByEmpIdService.execute(empId), employeeForm);
		return "update/update_input";
	}

	@RequestMapping(path = { "/update/check" }, method = { RequestMethod.POST })
	public String checkUpdate(@Valid @ModelAttribute EmployeeForm employeeForm, BindingResult result, Model model) {
		if (result.hasErrors())
			return "update/update_input";
		Department department = this.searchForDepartmentByDeptIdService.execute(employeeForm.getDeptId());
		model.addAttribute("deptName", department.getDeptName());
		return "update/update_check";
	}

	@RequestMapping(path = { "/update/back" }, method = { RequestMethod.POST })
	public String backInputUpdate(@ModelAttribute EmployeeForm employeeForm) {
		return "update/update_input";
	}

	@RequestMapping(path = { "/update/complete" }, method = { RequestMethod.POST })
	public String completeUpdate(EmployeeForm employeeForm, HttpSession session) {
		Employee employee = BeanCopy.copyFormToEmployee(employeeForm);
		if (employee.getAuthority() == null)
			employee.setAuthority(Constant.DEFAULT_AUTHORITY);
		this.updateEmployeeService.execute(employee);
		Employee user = (Employee) session.getAttribute("user");
		if (user.getEmpId() == employee.getEmpId())
			user.setEmpName(employee.getEmpName());
		return "redirect:/update/complete";
	}

	@RequestMapping(path = { "/update/complete" }, method = { RequestMethod.GET })
	public String completeUpdate() {
		return "update/update_complete";
	}
}