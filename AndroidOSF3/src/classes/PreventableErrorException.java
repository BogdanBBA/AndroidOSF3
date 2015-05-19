package classes;

public class PreventableErrorException extends Exception
{
	private static final long serialVersionUID = 5958328870040856626L;

	public PreventableErrorException()
	{
	}

	public PreventableErrorException(String detailMessage)
	{
		super(detailMessage);
	}

	public PreventableErrorException(Throwable throwable)
	{
		super(throwable);
	}

	public PreventableErrorException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}
}
