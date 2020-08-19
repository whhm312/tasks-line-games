package me.line.games.anonymous;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import me.line.games.common.exception.FailedCreateContentException;
import me.line.games.common.exception.ResourceNoContentException;

@Aspect
@Component
public class AnonymousAspect {

	@AfterReturning(value = "execution(* me.line.games.anonymous.dao.AnonymousPostDAO.insert(..))", returning = "returnValue")
	public void afterInsert(JoinPoint joinPoint, Object returnValue) throws Throwable {
		if (returnValue instanceof Integer) {
			if ((Integer) returnValue == 0) {
				throw new FailedCreateContentException("Failed to INSERT.");
			}
		}
	}

	@AfterReturning(value = "execution(* me.line.games.anonymous.dao.AnonymousPostDAO.update*(..))", returning = "returnValue")
	public void afterUpdate(JoinPoint joinPoint, Object returnValue) throws Throwable {
		if (returnValue instanceof Integer) {
			if ((Integer) returnValue == 0) {
				throw new ResourceNoContentException("Failed to UPDATE.");
			}
		}
	}

	@AfterReturning(value = "execution(* me.line.games.anonymous.dao.AnonymousPostDAO.delete*(..))", returning = "returnValue")
	public void afterDelete(JoinPoint joinPoint, Object returnValue) throws Throwable {
		if (returnValue instanceof Integer) {
			if ((Integer) returnValue == 0) {
				throw new ResourceNoContentException("Failed to DELETE.");
			}
		}
	}
}
