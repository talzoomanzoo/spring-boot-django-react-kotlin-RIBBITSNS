import { createTheme } from '@mui/material/styles';

const lightTheme = createTheme({
  palette: {
    mode: 'light', // Set the theme type to light
    primary: {
      main: '#008000', // Set your primary color
    },
    secondary: {
      main: '#228B22', // Set your secondary color
    },
    background:{
        
        paper: 'white' 
      },
    
  },
});

export default lightTheme;
