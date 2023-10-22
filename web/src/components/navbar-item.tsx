import React, { ReactElement } from 'react';
import { useNavigate } from 'react-router-dom';

type Props = {
  icon: ReactElement;
  pageName: string;
  selected: boolean;
  route: string;
};

const NavbarItem: React.FC<Props> = ({ icon, pageName, selected, route }) => {
  const navigate = useNavigate();

  const navigateToPage = () => {
    navigate(route);
  };

  return (
    <div
      className={
        selected
          ? 'bg-primaryColorHover flex h-full items-center gap-x-2 px-4'
          : 'hover:bg-primaryColorHover flex h-full items-center gap-x-2 px-4'
      }
      onClick={() => navigateToPage()}>
      {icon}
      <span className="text-textColor cursor-pointer text-lg font-semibold">
        {pageName}
      </span>
    </div>
  );
};

export default NavbarItem;
