import { useNavigate } from 'react-router-dom';

const Logo = () => {
  const navigate = useNavigate();

  const navigateToHome = () => {
    navigate('/');
  };

  return (
    <div
      id="logo"
      className="flex h-full w-full cursor-default items-center gap-x-2 px-4 sm:w-auto sm:px-8"
      onClick={() => navigateToHome()}>
      <img
        src="/logo.png"
        alt="logo"
        className="hidden h-12 w-12 sm:block"
      />
      <span className="text-xl font-bold text-textColor">HUNTD-Ai</span>
    </div>
  );
};

export default Logo;
