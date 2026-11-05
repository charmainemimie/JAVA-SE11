package business;
@FunctionalInterface
/**
 * T = Tipo/Clase del elemento/objeto sobre el que se aplicará la función
 * t = Objeto de tipo de la clase T sobre el que se aplica la función
 * R = Valor predeterminado a devolver si la función tira una excepción
 */
public interface FuncionValidadora<T,R> {
    R aplicar(T t) throws Exception;
}
