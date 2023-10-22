import { useNavigate } from 'react-router-dom';

const Logo = () => {
  const navigate = useNavigate();

  const navigateToHome = () => {
    navigate('/');
  };

  return (
    <div
      id="logo"
      className="flex h-full cursor-default items-center px-8"
      onClick={() => navigateToHome()}>
      <span className="text-textColor text-xl font-bold">HUNTD-Ai</span>
    </div>
  );
};

export default Logo;
