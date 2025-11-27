package jp.co.sss.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.crud.mapper.EmployeeMapper;

/**
 * 社員削除サービス
 */
@Service
public class DeleteEmployeeService {
	@Autowired
	private EmployeeMapper mapper;

	/**
	 * 社員削除
	 * 
	 * @return 社員エンティティ
	 */
	public Boolean execute(Integer empId) {
		return mapper.delete(empId);
	}

}
