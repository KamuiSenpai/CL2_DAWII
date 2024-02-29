package com.cibertec.assessment.service;

import java.util.List;

import com.cibertec.assessment.beans.SquareBean;
import com.cibertec.assessment.model.Square;

public interface SquareService {

	public Square create(Square s);
	
	public List<SquareBean> list();
}
