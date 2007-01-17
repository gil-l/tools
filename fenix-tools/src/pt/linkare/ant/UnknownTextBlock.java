package pt.linkare.ant;

public class UnknownTextBlock extends TextBlock {

    
    public boolean equals(Object other)
    {
	if(!(other instanceof UnknownTextBlock)) return false;
	
	UnknownTextBlock unknownTextBlock = ((UnknownTextBlock)other);
	if(getContent()==null || unknownTextBlock.getContent()==null) return false;
	
	return getContent().equals(unknownTextBlock.getContent());
    }
    
    public String toString()
    {
	return getContent();
    }

    @Override
    protected void initializeFromContent(String content) {
    }

    
   
}
