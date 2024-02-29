package com.cibertec.assessment.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.assessment.beans.SquareBean;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.repo.SquareRepo;
import com.cibertec.assessment.service.SquareService;

@Service
public class SquareServiceImpl implements SquareService {

	@Autowired
	SquareRepo squareRepo;

	// Método para guardar un cuadrado en la base de datos
	@Override
	public Square create(Square s) {
		// Verificar si el nuevo cuadrado se intersecta con algún otro cuadrado
		// existente
		List<Square> existingSquares = squareRepo.findAll();
		for (Square existingSquare : existingSquares) {
			if (doPolygonsIntersect(parseStringToIntArray(s.getXPoints()), parseStringToIntArray(s.getYPoints()),
					parseStringToIntArray(existingSquare.getXPoints()),
					parseStringToIntArray(existingSquare.getYPoints()))) {
				// Si hay intersección, actualiza o guarda la información relevante en el
				// cuadrado existente
				// Aquí se puede definir cómo deseas manejar la intersección
				// Por ejemplo, puedes agregar los IDs de los polígonos intersectados al
				// cuadrado existente
				String intersectedPolygons = combinePolygons(s.getPolygons(), existingSquare.getPolygons());
				existingSquare.setPolygons(intersectedPolygons);
				squareRepo.save(existingSquare);
			}
		}
		// Guardar el nuevo cuadrado en la base de datos
		return squareRepo.save(s);
	}

	// Método para obtener la lista de todos los cuadrados
	@Override
	public List<SquareBean> list() {
		// Obtener la lista de cuadrados desde la base de datos
		List<Square> list = squareRepo.findAll();
		List<SquareBean> listSquareBeans = new ArrayList<>();

		// Iterar sobre cada cuadrado y convertirlo en un SquareBean
		list.forEach(s -> {
			SquareBean squareBean = new SquareBean();
			squareBean.setId(s.getId());
			squareBean.setName(s.getName());
			// Convertir las coordenadas X e Y de string a array de enteros
			squareBean.setXPoints(parseStringToIntArray(s.getXPoints()));
			squareBean.setYPoints(parseStringToIntArray(s.getYPoints()));
			squareBean.setNpoints(s.getNpoints());
			// Convertir los IDs de polígonos de string a array de enteros
			squareBean.setPolygons(parseStringToIntArray(s.getPolygons()));
			// Agregar el SquareBean a la lista
			listSquareBeans.add(squareBean);
		});

		return listSquareBeans;
	}

	// Método para convertir un string de coordenadas a un array de enteros
	private Integer[] parseStringToIntArray(String points) {
		if (points == null) {
			// Manejar el caso en el que la cadena sea null
			return new Integer[0]; // O cualquier otro manejo que consideres adecuado
		}
		String[] parts = points.substring(1, points.length() - 1).split(", ");
		return Arrays.stream(parts).map(Integer::parseInt).toArray(Integer[]::new);
	}

	// Método para verificar si dos polígonos se intersectan
	private boolean doPolygonsIntersect(Integer[] x1, Integer[] y1, Integer[] x2, Integer[] y2) {
		// Verificar si algún vértice de un polígono está dentro del otro polígono
		for (int i = 0; i < x1.length; i++) {
			if (isPointInsidePolygon(x1[i], y1[i], x2, y2)) {
				return true;
			}
		}
		for (int i = 0; i < x2.length; i++) {
			if (isPointInsidePolygon(x2[i], y2[i], x1, y1)) {
				return true;
			}
		}
		return false;
	}

	// Método para determinar si un punto está dentro de un polígono
	private boolean isPointInsidePolygon(int x, int y, Integer[] xPoints, Integer[] yPoints) {
		int i, j;
		boolean result = false;
		for (i = 0, j = xPoints.length - 1; i < xPoints.length; j = i++) {
			if ((yPoints[i] > y) != (yPoints[j] > y)
					&& (x < (xPoints[j] - xPoints[i]) * (y - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i])) {
				result = !result;
			}
		}
		return result;
	}

	// Método para combinar los IDs de los polígonos intersectados
	private String combinePolygons(String polygons1, String polygons2) {
		// Convertir los strings de polígonos a listas de IDs de polígonos
		List<String> polygonList1 = Arrays.asList(polygons1.substring(1, polygons1.length() - 1).split(", "));
		List<String> polygonList2 = Arrays.asList(polygons2.substring(1, polygons2.length() - 1).split(", "));

		// Unir las dos listas de IDs de polígonos
		Set<String> combinedPolygons = new HashSet<>(polygonList1);
		combinedPolygons.addAll(polygonList2);

		// Convertir la lista combinada de IDs de polígonos de nuevo a un string
		return "[" + String.join(", ", combinedPolygons) + "]";
	}
}