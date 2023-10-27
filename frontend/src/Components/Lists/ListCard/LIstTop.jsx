import { memo } from "react";
import { useDispatch, useSelector } from "react-redux";
import ListCard from "./ListCard";

const ListTop = memo(({ list }) => {
    const { theme } = useSelector((store) => store);
    const dispatch = useDispatch();
    // useEffect(() => {
    //     dispatch(getAllLists());
    // }, [])
    return (

        <div
            className="space-y-3"
            style={{ marginTop: 10 }}>

            공개 리스트
            <hr
                style={{
                    marginTop: 10,
                    background: 'grey',
                    color: 'grey',
                    borderColor: 'grey',
                    height: '1px',
                }}
            />

            <section
                className={`${theme.currentTheme === "dark" ? "pt-14" : ""} space-y-5`}>
                {list?.lists?.map((item) => (
                    !item.privateMode ? (
                        <ListCard
                            style={{ marginTop: 10 }}
                            list={item} />
                    ) : null
                ))}
            </section>
        </div>
    );
});

export default ListTop;